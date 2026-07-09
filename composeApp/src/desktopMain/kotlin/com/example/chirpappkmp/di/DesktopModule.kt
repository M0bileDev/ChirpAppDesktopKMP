package com.example.chirpappkmp.di

import com.example.chirpappkmp.ApplicationStateHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val desktopModule = module {
    singleOf(::ApplicationStateHolder)
}