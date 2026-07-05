package com.example.core.data.di

import com.example.core.data.auth.createDataStore
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single {
        createDataStore()
    }
}