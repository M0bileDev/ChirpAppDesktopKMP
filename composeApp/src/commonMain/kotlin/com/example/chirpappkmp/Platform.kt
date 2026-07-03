package com.example.chirpappkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform