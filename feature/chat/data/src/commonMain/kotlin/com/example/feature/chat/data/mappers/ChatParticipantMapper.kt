package com.example.feature.chat.data.mappers

import com.example.feature.chat.data.dto.ChatParticipantDto
import com.example.feature.chat.database.entities.ChatParticipantEntity
import com.example.feature.chat.domain.model.ChatParticipant

fun ChatParticipantDto.toDomain(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatParticipantEntity.toDomain(): ChatParticipant{
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl =  profilePictureUrl
    )
}

fun ChatParticipant.toEntity(): ChatParticipantEntity{
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}