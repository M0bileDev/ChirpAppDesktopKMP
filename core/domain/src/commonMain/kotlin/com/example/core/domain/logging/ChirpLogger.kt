package com.example.core.domain.logging

interface ChirpLogger {
    fun info(message: String)
    fun warning(message: String)
    fun debug(message: String)
    fun error(message: String, throwable: Throwable? = null)
}