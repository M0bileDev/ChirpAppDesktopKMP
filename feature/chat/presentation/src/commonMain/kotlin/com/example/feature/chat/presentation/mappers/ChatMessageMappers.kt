package com.example.feature.chat.presentation.mappers

import com.example.feature.chat.domain.model.MessageWithSender
import com.example.feature.chat.presentation.model.MessageUi
import com.example.feature.chat.presentation.util.DateUtils
import com.example.feature.chat.presentation.util.DateUtils.formatMessageTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun MessageWithSender.toUi(
    localUserId: String,
): MessageUi {
    val isFromLocalUser = sender.userId == localUserId

    return if (isFromLocalUser) {
        MessageUi.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            formattedSentAt = formatMessageTime(
                instant = message.createdAt
            ),
        )
    } else {
        MessageUi.OtherUserMessage(
            id = message.id,
            content = message.content,
            formattedSentAt = formatMessageTime(instant = message.createdAt),
            sender = sender.toUi()
        )
    }
}

/**
 * First sort messages by descending order, then group by "created at" date and finally
 * add separator to each group
 */
fun List<MessageWithSender>.toUiList(localUserId: String): List<MessageUi> {
    return sortedByDescending { messageWithSender ->
        messageWithSender.message.createdAt
    }.groupBy { sortedMessageWithSender ->
        sortedMessageWithSender.message.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }.flatMap { (dateKey, messageValue) ->
        messageValue.map { groupedMessage -> groupedMessage.toUi(localUserId) } + MessageUi.DateSeparator(
            id = dateKey.toString(),
            date = DateUtils.formatDateSeparator(dateKey)
        )
    }
}