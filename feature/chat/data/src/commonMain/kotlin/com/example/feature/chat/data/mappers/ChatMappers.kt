package com.example.feature.chat.data.mappers

import com.example.feature.chat.data.dto.ChatDto
import com.example.feature.chat.database.entities.ChatEntity
import com.example.feature.chat.database.entities.ChatInfoEntity
import com.example.feature.chat.database.entities.ChatWithParticipants
import com.example.feature.chat.database.entities.MessageWithSender
import com.example.feature.chat.domain.model.Chat
import com.example.feature.chat.domain.model.ChatInfo
import com.example.feature.chat.domain.model.ChatMessage
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import com.example.feature.chat.domain.model.ChatParticipant
import kotlin.time.Instant

typealias DataMessageWithSender = MessageWithSender
typealias DomainMessageWithSender = com.example.feature.chat.domain.model.MessageWithSender

fun ChatDto.toDomain(): Chat {
    val lastMessageSenderUsername = lastMessage?.let { messageDto ->
        participants.find { it.userId == messageDto.senderId }?.username
    }
    return Chat(
        id = id,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.parse(lastActivityAt),
        lastMessage = lastMessage?.toDomain(),
        lastMessageSenderUsername = lastMessageSenderUsername
    )
}

fun ChatWithParticipants.toDomain(): Chat {
    return Chat(
        id = chat.chatId,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.fromEpochMilliseconds(chat.lastActivityAt),
        lastMessage = lastMessage?.toDomain(),
        lastMessageSenderUsername = lastMessage?.senderUsername
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        lastActivityAt = lastActivityAt.toEpochMilliseconds()
    )
}

fun ChatEntity.toDomain(
    participants: List<ChatParticipant>,
    lastMessage: ChatMessage? = null
): Chat {
    val lastMessageSenderUsername = lastMessage?.let { messageDto ->
        participants.find { it.userId == messageDto.senderId }?.username
    }
    return Chat(
        id = chatId,
        participants = participants,
        lastActivityAt = Instant.fromEpochMilliseconds(lastActivityAt),
        lastMessage = lastMessage,
        lastMessageSenderUsername = lastMessageSenderUsername
    )
}

fun DataMessageWithSender.toDomain(): DomainMessageWithSender {
    return DomainMessageWithSender(
        message = message.toDomain(),
        sender = sender.toDomain(),
        status = ChatMessageDeliveryStatus.valueOf(this.message.deliveryStatus)
    )
}

fun ChatInfoEntity.toDomain(): ChatInfo {
    return ChatInfo(
        chat = chat.toDomain(
            participants = this.participants.map { it.toDomain() }
        ),
        messagesWithSenders = messagesWithSenders.map { it.toDomain() }
    )
}