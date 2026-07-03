package com.example.core.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/**
 * Maintain view model references scoped to dialogs / bottom sheets
 */
class ScopedStoreRegistryViewModel : ViewModel() {

    private val stores = mutableMapOf<String, ViewModelStore>()

    fun getOrCreate(id: String): ViewModelStore =
        stores.getOrPut(id) { ViewModelStore() }

    fun clear(id: String) = stores.remove(id)?.clear()

    override fun onCleared() {
        super.onCleared()
        stores.values.forEach { store -> store.clear() }
        stores.clear()
    }
}