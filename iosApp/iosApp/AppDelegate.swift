//
//  AppDelegate.swift
//  iosApp
//
//  Created by Damian Ogórek on 27/06/2026.
//

import ComposeApp
import FirebaseCore
import FirebaseMessaging
import Foundation
import UIKit
import UserNotifications

//Create similar implementation of android ChirpFirebaseMessagingService

// NSObject -> bridge Swift with ObjC
// UIApplicationDelegate -> handle lifecycle events (app's foreground or background)
// UNUserNotificationCenterDelegate -> how notifications are presented and how user interacts with them
// MessagingDelegate -> handles token changes
class AppDelegate: NSObject, UIApplicationDelegate,
    UNUserNotificationCenterDelegate, MessagingDelegate
{

    // called when app is initializing, initialize Firebase Messaging on ios side
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication
            .LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()

        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self

        return true
    }

    // called after register for remote notification on the Kotlin side -> registerForRemoteNotifications()
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken

        refreshToken()
    }

    //when registration for fcm token failed
    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: any Error
    ) {
        print(
            "iOS Failed to register for push notifications: \(error.localizedDescription)"
        )
    }

    // when new token has received
    func messaging(
        _ messaging: Messaging,
        didReceiveRegistrationToken fcmToken: String?
    ) {
        guard let token = fcmToken, !token.isEmpty else {
            refreshToken()
            return
        }

        // when fcmToken is not null, and not empty (guard conditions) -> update user default with refreshed token
        UserDefaults.standard.set(fcmToken, forKey: "FCM_TOKEN")
        IosDeviceTokenHolderBridge.shared.updateToken(token: fcmToken)
    }

    func refreshToken() {
        Task {
            do {
                // get token async
                let fcmToken = try await Messaging.messaging().token()
                // update user default
                UserDefaults.standard.set(fcmToken, forKey: "FCM_TOKEN")
                IosDeviceTokenHolderBridge.shared.updateToken(token: fcmToken)
            } catch {
                print("iOS getting FCM token: \(error.localizedDescription)")
            }
        }
    }

    // when push notification was received and app was in the background
    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable: Any],
        fetchCompletionHandler completionHandler:
            @escaping (UIBackgroundFetchResult) -> Void
    ) {

        // forward notification to Firebase
        // userInfo -> contains raw notification body
        // Firebase takes userInfo and display notification
        Messaging.messaging().appDidReceiveMessage(userInfo)

        // notify iOS that notification was successfully handled
        completionHandler(.newData)
    }

    // when push notification was received in the foreground
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler:
            @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        //how we would show the notification
        //.sound -> with sound
        //.badge -> with badge
        //.banner -> with banner
        completionHandler([.banner])
    }

    // when user taps notification -> deeplink to detail screen
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        //from response -> extract json payload like chatID
        let userInfo = response.notification.request.content.userInfo

        if let chatId = userInfo["chatId"] as? String {
            let deepLinkUrl = "chirp://chat_detail/\(chatId)"
            ExternalUriHandler.shared.onNewUri(uri: deepLinkUrl)
        }

        // notify iOS that notification tap was successfully handled
        completionHandler()
    }

}
