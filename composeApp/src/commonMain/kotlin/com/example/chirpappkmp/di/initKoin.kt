package com.example.chirpappkmp.di

import com.example.core.data.di.coreDataModule
import com.example.core.presentation.di.corePresentationModule
import com.example.feature.auth.presentation.di.authPresentationModule
import com.example.feature.chat.data.di.chatDataModule
import com.example.feature.chat.presentation.di.chatPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            authPresentationModule,
            appModule,
            chatPresentationModule,
            corePresentationModule,
            chatDataModule
        )
    }
}