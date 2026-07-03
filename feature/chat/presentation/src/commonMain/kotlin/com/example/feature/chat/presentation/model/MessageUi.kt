package com.example.feature.chat.presentation.model

import com.example.core.presentation.util.UiText
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi

sealed class MessageUi(open val id: String) {
    data class LocalUserMessage(
        override val id: String,
        val content: String,
        val deliveryStatus: ChatMessageDeliveryStatus,
        val formattedSentAt: UiText,
    ) : MessageUi(id)

    data class OtherUserMessage(
        override val id: String,
        val content: String,
        val formattedSentAt: UiText,
        val sender: ChatParticipantUi
    ) : MessageUi(id)

    data class DateSeparator(
        override val id: String,
        val date: UiText
    ) : MessageUi(id)
}