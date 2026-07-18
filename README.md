# Chirp — a Kotlin Multiplatform chat client

**Android · iOS · Windows · macOS · Linux — one codebase, one UI, ~88% shared Kotlin.**

Chirp is a realtime chat application built to answer a specific engineering question: *how far can Compose Multiplatform actually go before you have to write platform code?* Not a toy sample — it ships auth with token refresh, an offline-first cache, a reconnecting WebSocket layer, push notifications on three platforms, deep links, adaptive layouts, and native desktop packaging.

The answer, measured on this codebase, is **88%**. The remaining 12% is documented below, along with *why* each piece resisted sharing — which is the more interesting half of the story.

---

## Table of contents

- [What it does](#what-it-does)
- [Code sharing, measured](#code-sharing-measured)
- [Architecture](#architecture)
- [Convention plugins — the build as a first-class module](#convention-plugins--the-build-as-a-first-class-module)
- [Tech stack](#tech-stack)
- [Engineering challenges](#engineering-challenges)
- [What needed platform-specific implementation](#what-needed-platform-specific-implementation)
- [Running the project](#running-the-project)

---

## What it does

**Authentication** — register, e-mail verification, login, forgot/reset password. Access tokens refresh transparently inside a Ktor auth plugin; session expiry propagates up as an event that resets the nav graph to the auth flow.

**Realtime chat** — chat list, chat detail, chat creation, participant management. Messages arrive over a WebSocket that reconnects with backoff, observes OS-level connectivity, and reconciles against the local cache on resume.

**Offline-first** — every repository is an `OfflineFirst*Repository`: reads come from Room immediately and emit as `Flow`, network results are merged in behind them. The app is fully navigable with no connection; messages composed offline carry a delivery status through the UI.

**Adaptive UI** — one Compose tree that resolves to a single-pane list on a phone and a list-detail pane scaffold on a tablet or desktop window, driven by window size class rather than by platform.

**Push notifications** — Firebase Cloud Messaging on Android, APNs device tokens bridged from Swift on iOS, and a native system-tray notifier on desktop built from the same WebSocket message flow.

**Deep links** — `chirp://` custom scheme plus verified `https://chirp.pl-coding.com` app links, handled on all three platforms, including cold-start-from-link on desktop.

**Desktop as a real target, not a checkbox** — multiple independent windows sharing one process and one Koin graph, system tray menu, OS theme detection, drag-and-drop image upload, single-instance launch, and signed installers (MSIX / `.deb` / `.app`) produced by Conveyor.

---

## Code sharing, measured

Kotlin line counts across all source sets (excluding build output, comments included):

| Source set | Files | Lines | Share |
| --- | ---: | ---: | ---: |
| `commonMain` — shared by every target | 250 | 15,560 | **88.0%** |
| `desktopMain` | 34 | 982 | 5.6% |
| `iosMain` | 15 | 538 | 3.0% |
| `androidMain` | 14 | 407 | 2.3% |
| `mobileMain` — Android + iOS only | 3 | 72 | 0.4% |
| `jvmCommonMain` — Android + desktop only | 1 | 38 | 0.2% |
| `androidApp` host (Kotlin) | 2 | 81 | 0.5% |

Plus **178 lines of Swift** in `iosApp/` — the entire native iOS host: `iOSApp.swift`, `AppDelegate.swift` (APNs registration), `ContentView.swift` (the `UIViewController` bridge).

The whole design system — 3,279 lines of `Chirp*` Compose components — is 100% common. So is every ViewModel, every screen, every mapper, the entire domain layer, and the Room schema. **`feature/auth/presentation` (2,194 lines) and `feature/chat/domain` contain zero platform-specific code.**

Two intermediate source sets do real work here rather than existing for tidiness:

- **`mobileMain`** (Android + iOS, excluding desktop) — notification permission handling and mobile image pickers. Desktop has neither a permission model nor a photo picker, so this code cannot live in `commonMain`, but duplicating it into two source sets would be worse.
- **`jvmCommonMain`** (Android + desktop, excluding iOS) — lets both JVM targets share the OkHttp Ktor engine while iOS uses Darwin.

Both come from a custom `KotlinHierarchyTemplate` (`build-logic/.../HierarchyTemplate.kt`) applied to every module by the convention plugin.

---

## Architecture

Strict **layered, per-feature** modularisation. Eleven Gradle modules, dependencies pointing only downward:

```
presentation ──→ domain ←── data ──→ database
                   ↑
              everything may depend on core/*
```

`domain` holds interfaces and models with no platform or framework dependencies — no Ktor, no Room, no Compose. `data` implements those interfaces. `presentation` depends on `domain` only, never on `data`, so the DI graph is the single place where an implementation is bound to an interface.

```
composeApp/          shared entry point — App.kt, NavigationRoot, MainViewModel
  desktopMain/       desktop main(), multi-window state, tray menu, deep links
  iosMain/           MainViewController + APNs device-token bridge
androidApp/          Android host: Application, MainActivity, manifest, icons
iosApp/              Xcode project consuming the shared framework

core/
  domain/            Result / DataError, auth models, PasswordValidator, Paginator
  data/              Ktor auth service + client factory, DataStore session storage,
                     theme preferences, platform utilities
  presentation/      permission controllers, UiText, adaptive helpers, ObserveAsEvents
  designsystem/      Chirp* component library + theme  (3,279 lines, 100% common)

feature/
  auth/domain        validators
  auth/presentation  login, register, verification, forgot / reset password
  chat/domain        repository & service interfaces, domain models
  chat/data          Ktor services, OfflineFirst* repositories, DTOs + mappers,
                     WebSocket client, FCM / APNs, connectivity + retry handling
  chat/database      Room KMP database, DAOs, entities, exported schemas
  chat/presentation  chat list, detail, adaptive list-detail, profile, manage chat
```

### MVI presentation

Every screen folder is the same four files — `XxxViewModel`, `XxxState`, `XxxAction`, and `XxxEvent` where one-shot effects are needed. State is a single immutable data class exposed as `StateFlow`; the UI sends `Action`s upward; `Event`s (navigation, snackbars) are consumed once via `ObserveAsEvents`. Screens are written as stateless composables taking `state` + `onAction`, which is what makes them previewable and identical across all three platforms.

### Dependency injection

Koin, one module per layer (`coreDataModule`, `chatDataModule`, `chatPresentationModule`, …), aggregated in `initKoin()`. Platform bindings are `expect val platformCoreDataModule: Module` with per-target actuals — so the *shape* of the graph is common and only the leaves differ. Desktop layers an extra `desktopModule` on top at `main()`.

---

## Convention plugins — the build as a first-class module

With eleven KMP modules, copy-pasted Gradle config becomes the maintenance bottleneck. All shared build logic lives in an included build, `build-logic/`, as typed Gradle plugins:

| Plugin | Responsibility |
| --- | --- |
| `convention.kmp.library` | Android + iOS + desktop targets, hierarchy template, compiler opts |
| `convention.cmp.library` | `kmp.library` + Compose Multiplatform |
| `convention.cmp.feature` | `cmp.library` + Koin, navigation, viewmodel, `core:presentation`, `core:designsystem` |
| `convention.cmp.application` | the `composeApp` aggregate |
| `convention.android.application` | the `androidApp` host |
| `convention.room` | KSP + Room, registers `kspAndroid` / `kspIosSimulatorArm64` / `kspIosArm64`, schema export |
| `convention.buildkonfig` | generates `BuildKonfig` with `API_KEY`, failing fast if absent |

The result: a feature module's entire build file is a plugin alias plus its dependency list. Two details worth calling out:

- **Namespaces and iOS framework names are derived from the module path**, not hardcoded — `:core:domain` becomes package `com.example.core.domain` and framework `CoreDomain`. Adding a module requires no naming ceremony.
- `validatePlugins { enableStricterValidation = true; failOnWarning = true }` — the build logic itself is linted.

Dependencies come from the `gradle/libs.versions.toml` version catalog, with typesafe `projects.*` accessors for module references. Configuration cache and build cache are on.

---

## Tech stack

| Concern | Choice |
| --- | --- |
| Language | Kotlin `2.4.10`, coroutines + `Flow` throughout |
| UI | Compose Multiplatform `1.11.1`, Material 3 adaptive, Compose Hot Reload |
| Navigation | Jetbrains Compose Navigation with type-safe routes |
| DI | Koin `4.2.2` |
| Networking | Ktor `3.5.1` — OkHttp engine on JVM/Android, Darwin on iOS — plus WebSockets |
| Persistence | Room KMP `2.8.4` with bundled SQLite; DataStore Preferences for session + theme |
| Serialization | `kotlinx.serialization` |
| Images | Coil 3 with the Ktor network fetcher |
| Logging | Kermit |
| Permissions | MOKO permissions (mobile source set only) |
| Push | Firebase Cloud Messaging (Android), APNs (iOS), AWT tray (desktop) |
| Build | Gradle convention plugins in an included build, AGP `9.2.1`, BuildKonfig |
| Packaging | Conveyor `2.0` — MSIX, `.deb`, `.app` |

---

## Engineering challenges

### 1. iOS network errors arrive disguised as coroutine cancellations

The hardest bug in the project. On iOS, a dropped connection surfaces through Ktor's Darwin engine as a `CancellationException` — indistinguishable, at the common-code level, from a user navigating away and cancelling a scope. Treating it as cancellation meant the WebSocket silently stopped retrying; treating cancellation as a network error meant retry storms on every screen exit.

The fix walks the exception cause chain looking for `DarwinHttpRequestException`, extracts its underlying `NSError`, and matches the code against `NSURLErrorNotConnectedToInternet` / `NetworkConnectionLost` / `TimedOut`. Genuine network failures are re-wrapped as a distinct `IOSNetworkCancellationException` before crossing back into common code, where the retry handler can treat them as retriable without ever seeing a platform type.

```kotlin
private fun Throwable.extractNsError(): NSError? =
    findInCauseChain<DarwinHttpRequestException>()?.origin
```

A cinterop binding to Apple's `Network` framework (`network.def`) backs the connectivity observer on the same platform.

### 2. Multi-window desktop with a single shared graph

Desktop users expect several chat windows. Compose Desktop gives you `Window`, but nothing about *state* across windows. `ApplicationStateHolder` owns a list of window descriptors and the tray state; each window is keyed by id, tracks its own focus, and closing the last one exits the application. All windows share one Koin graph, one WebSocket, and one Room instance — so a message received once updates every open window, and notifications suppress themselves when *any* window has focus.

### 3. Deep links across three completely different delivery mechanisms

Android delivers links as `Intent`s to an activity, iOS through `AppDelegate` callbacks, desktop through AWT's `Desktop.setOpenURIHandler` — *or* as `argv` when the link cold-starts the process. Worse, on every platform the link can arrive before the navigation graph exists.

`ExternalUriHandler` solves this with a tiny caching relay in common code: URIs arriving early are held until a listener registers, then replayed and cleared. Each platform's job shrinks to calling one function.

### 4. Room on Kotlin/Native

Room's KMP support imposes a rule the Android compiler doesn't enforce: in source sets targeting non-Android platforms, every `@Query` must be `suspend` or return an observable type. A plain blocking DAO method compiles perfectly on Android and then fails iOS KSP with a *misleading* `Unused parameter` error alongside the real one. Every DAO change here is validated against `kspKotlinIosSimulatorArm64`, not the Android build.

Database placement is itself platform work: `Room.databaseBuilder` needs a `Context` on Android, an `NSHomeDirectory()` path on iOS, and on desktop a hand-rolled per-OS application directory (`%APPDATA%`, `~/Library/Application Support`, `~/.local/share`).

### 5. Notification parity without a notification framework

Desktop has no FCM. Rather than special-casing the UI, `DesktopNotifier` derives notifications from the *same* `chatConnectionClient.chatMessages` flow the chat screen consumes — filtering out the current user's own messages, deduplicating by message id, and resolving participant names from the local cache. Push on mobile and tray notifications on desktop are two adapters over one common event stream.

### 6. Keeping the design system genuinely common

3,279 lines of components with zero platform code was a constraint, not an accident: no `android.graphics`, no `UIKit`, no AWT anywhere in `core:designsystem`. Adaptive dialog-vs-bottom-sheet behaviour is chosen by window size class rather than by platform, which is why a desktop window narrowed to phone width behaves like a phone.

---

## What needed platform-specific implementation

Fourteen `expect` declarations, and the reason each one exists:

| Declaration | Why it can't be common |
| --- | --- |
| `ChirpDatabaseFactory` | Room needs `Context` / `NSHomeDirectory` / a per-OS file path |
| `ChirpChatDatabaseConstructor` | KSP-generated per target |
| `createDataStore` | same problem, different storage roots |
| `ConnectivityObserver` | `ConnectivityManager` / `NWPathMonitor` (via cinterop) / JVM polling |
| `ConnectionErrorHandler` | see challenge 1 — Darwin `NSError` unwrapping |
| `platformSafeCall` | wraps engine-specific exception types |
| `AppLifecycleObserver` | `ProcessLifecycleOwner` / `UIApplication` notifications / window focus |
| `FirebasePushNotificationService` | FCM / APNs token bridge / no-op on desktop |
| `PermissionController` + `rememberPermissionController` | MOKO on mobile; desktop has no runtime permissions |
| `rememberImagePicker` | Photo Picker / `PHPickerViewController` / AWT file dialog |
| `rememberDragAndDropTarget` | desktop-only capability, no-op elsewhere |
| `PlatformUtils` | OS identification |
| `platformCoreDataModule` / `platformChatDataModule` | bind the above into Koin |

The pattern throughout: **the interface is common, the leaf is platform.** No `expect` here returns a platform type into common code — everything is mapped to a domain model at the boundary, which is what kept the ViewModels and screens completely target-agnostic.

---

## Running the project

**Prerequisites** — JDK 21, Android SDK (`compileSdk 36`, `minSdk 26`), Xcode on Apple Silicon for iOS (`iosArm64` / `iosSimulatorArm64` are configured).

Add an API key to `local.properties` — the build fails fast without it:

```properties
API_KEY=<your key>
```

```bash
./gradlew :composeApp:run                # Desktop
./gradlew :composeApp:hotRunDesktop      # Desktop with Compose Hot Reload
./gradlew :androidApp:installDebug       # Android
./gradlew build                          # everything
```

**iOS** — open `iosApp/iosApp.xcodeproj` in Xcode and run.

**Desktop installers** — `conveyor make site` using `conveyor.conf` (MSIX / `.deb` / `.app`). Compose Desktop's `packageDistributionForCurrentOS` also works.

**Validating DAO changes** — always against iOS, since Android won't catch the violation:

```bash
./gradlew :feature:chat:database:kspKotlinIosSimulatorArm64
```

The backend is a hosted service at `chirp.pl-coding.com` (REST + WebSocket); no local server setup is required.

---

## License

MIT © Mobidev Studio
