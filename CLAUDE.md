# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

ChirpAppKMP is a Kotlin Multiplatform chat app sharing UI via Compose Multiplatform. The build wires **Android** (`androidMain`) and **iOS** (`iosArm64`, `iosSimulatorArm64`) targets — see `build-logic/.../KotlinMultiplatform.kt` and `KotlinIosTargets.kt`. (The README also mentions Desktop/JVM, but the convention plugins do not currently configure a JVM target.)

## Build & Run

```bash
./gradlew :composeApp:assembleDebug          # Build Android APK
./gradlew :feature:chat:database:kspKotlinIosSimulatorArm64   # Run Room KSP for an iOS target (catches DAO errors)
./gradlew build                              # Build everything
```

- **iOS**: open `iosApp/` in Xcode and run, or use the IDE run config. The Kotlin shared code is exposed as a framework whose `baseName` is derived from the module path (e.g. `:core:domain` → `CoreDomain`).
- **API_KEY is required to build.** `BuildKonfigConventionPlugin` reads `API_KEY` from `local.properties` and fails the build if missing.
- No test source sets exist in the repo yet; there is no test command to run.

## Module Architecture

The project is split into many small Gradle modules following a strict **layered, per-feature** structure. Module dependencies only point "downward": `presentation → domain ← data → database`, and everything may depend on `core/*`.

- `composeApp` — the application entry point (`App.kt`, `NavigationRoot.kt`). Aggregates all feature/core modules and starts Koin via `initKoin()`.
- `core/{domain,data,presentation,designsystem}` — shared building blocks. `designsystem` holds reusable `Chirp*` Compose components.
- `feature/<name>/presentation` — Compose screens + ViewModels.
- `feature/<name>/domain` — interfaces (`*Repository`, `*Service`), domain models, no platform deps.
- `feature/<name>/data` — implementations: Ktor services (`Ktor*Service`), `OfflineFirst*Repository`, DTOs + mappers, WebSocket clients.
- `feature/chat/database` — Room (KMP) database, DAOs, entities. Schemas are exported to `feature/chat/database/schemas/`.

### Convention plugins (`build-logic/`)

Module build files are intentionally tiny because shared config lives in `build-logic/convention/` as plugins applied by id. When adding a module, apply the matching plugin instead of duplicating config:

- `convention.kmp.library` — base KMP library (Android + iOS targets, namespace/framework name derived from module path).
- `convention.cmp.library` — KMP library + Compose Multiplatform deps.
- `convention.cmp.feature` — `cmp.library` + Koin, Compose navigation/viewmodel, and `core:presentation`/`core:designsystem`. Use for `feature/*/presentation`.
- `convention.room` — applies KSP + `androidx.room`, registers `kspAndroid`/`kspIosSimulatorArm64`/`kspIosArm64`, sets the schema dir.
- `convention.buildkonfig` — generates `BuildKonfig` with `API_KEY` (package name derived from module path).
- `convention.cmp.application` — the `composeApp` itself.

Dependencies are managed through the version catalog `gradle/libs.versions.toml` (use `projects.*` typesafe accessors for module deps).

## Key Patterns & Conventions

- **MVI presentation**: each screen folder contains `XxxViewModel`, `XxxState`, `XxxAction`, and (when needed) `XxxEvent`. Follow this naming when adding screens.
- **DI is Koin**, one module per layer (`chatDataModule`, `chatPresentationModule`, `corePresentationModule`, etc.). New modules must be registered in `composeApp/.../di/initKoin.kt`. Platform-specific bindings use `expect`/`actual` module declarations (e.g. `CoreDataModule.android.kt` / `.ios.kt`).
- **expect/actual**: platform code lives in `Platform.kt` (common) with `Platform.android.kt` / `Platform.ios.kt` actuals. iOS data layer also uses a cinterop (`network.def`) for native networking.
- **Repositories are offline-first**: `OfflineFirst*Repository` read/write the Room cache and reconcile with Ktor/WebSocket services.

### Room on KMP — DAO gotcha

For source sets targeting non-Android platforms (iOS), Room requires every `@Query` method to be **either `suspend` or return an observable type (`Flow`)**. A plain blocking query method like `fun getX(id: String): T?` fails `kspKotlinIosSimulatorArm64` with two errors: `Only suspend functions are allowed in DAOs declared in source sets targeting non-Android platforms` and a misleading `Unused parameter: <name>`. Fix by making the method `suspend` (or returning `Flow`). Always validate DAO changes with the iOS KSP task, not just the Android build.
