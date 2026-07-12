package com.example.feature.chat.data.notification

import com.example.core.domain.auth.SessionStorage
import com.example.feature.chat.domain.chat.ChatConnectionClient
import com.example.feature.chat.domain.chat.ChatRepository

class DesktopNotifier(
    private val chatConnectionClient: ChatConnectionClient,
    private val sessionStorage: SessionStorage,
    private val chatRepository: ChatRepository
) {
}