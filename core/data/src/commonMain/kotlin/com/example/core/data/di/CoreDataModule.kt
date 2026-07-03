package com.example.core.data.di

import com.example.core.data.auth.DataStoreStorageSession
import com.example.core.data.auth.KtorAuthService
import com.example.core.data.logging.KermitLogger
import com.example.core.data.network.HttpClientFactory
import com.example.core.domain.auth.AuthService
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.logging.ChirpLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {

    includes(platformCoreDataModule)

    single<ChirpLogger> { KermitLogger }
    single {
        HttpClientFactory(get(), get()).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreStorageSession) bind SessionStorage::class
}