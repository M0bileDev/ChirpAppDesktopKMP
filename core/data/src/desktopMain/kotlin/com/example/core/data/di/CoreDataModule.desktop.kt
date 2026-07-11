package com.example.core.data.di

import com.example.core.data.auth.createDataStore
import com.example.core.data.preferences.DataStoreThemePreferences
import com.example.core.domain.preferences.ThemePreferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single {
        createDataStore()
    }
    single<HttpClientEngine> { OkHttp.create() }
    singleOf(::DataStoreThemePreferences) bind ThemePreferences::class
}