package com.example.core.presentation.di

import com.example.core.presentation.util.ScopedStoreRegistryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val corePresentationModule = module {
    viewModelOf(::ScopedStoreRegistryViewModel)
}