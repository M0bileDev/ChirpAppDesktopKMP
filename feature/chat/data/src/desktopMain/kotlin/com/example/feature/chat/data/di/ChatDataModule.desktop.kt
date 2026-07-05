package com.example.feature.chat.data.di

import com.example.feature.chat.database.ChirpDatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformChatDataModule: Module = module {
    single { ChirpDatabaseFactory() }
}