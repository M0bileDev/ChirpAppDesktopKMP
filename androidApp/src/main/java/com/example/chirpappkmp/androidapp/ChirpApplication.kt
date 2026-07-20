package com.example.chirpappkmp.androidapp

import android.app.Application
import com.example.chirpappkmp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ChirpApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@ChirpApplication)
            androidLogger()
        }
    }
}