package com.example.feature.chat.presentation.mappers

import com.example.core.domain.auth.User
import com.example.feature.chat.domain.model.ChatParticipant
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi

fun ChatParticipant.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl
    )
}

fun User.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = id,
        username = username,
        initials = username.take(2).uppercase(),
        imageUrl = profilePictureUrl
    )
}