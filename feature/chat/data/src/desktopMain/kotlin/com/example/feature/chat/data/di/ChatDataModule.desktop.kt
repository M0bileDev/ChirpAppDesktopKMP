package com.example.feature.chat.data.di

import com.example.feature.chat.data.lifecycle.AppLifecycleObserver
import com.example.feature.chat.data.network.ConnectionErrorHandler
import com.example.feature.chat.data.network.ConnectivityObserver
import com.example.feature.chat.data.notification.FirebasePushNotificationService
import com.example.feature.chat.database.ChirpDatabaseFactory
import com.example.feature.chat.domain.notification.PushNotificationTokenService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformChatDataModule: Module = module {
    single { ChirpDatabaseFactory() }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)
    singleOf(::FirebasePushNotificationService) bind PushNotificationTokenService::class
}