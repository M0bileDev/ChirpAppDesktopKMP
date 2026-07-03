package com.example.feature.chat.data.participant

import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.onSuccess
import com.example.feature.chat.domain.model.ChatParticipant
import com.example.feature.chat.domain.participant.ChatParticipantRepository
import com.example.feature.chat.domain.participant.ChatParticipantService
import kotlinx.coroutines.flow.first

class OfflineFirstChatParticipantRepository(
    private val sessionStorage: SessionStorage,
    private val chatParticipantService: ChatParticipantService
) : ChatParticipantRepository {

    override suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError> {
        return chatParticipantService
            .getLocalParticipant()
            .onSuccess { participant ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                with(participant) {
                    sessionStorage.set(
                        currentAuthInfo?.copy(
                            user = currentAuthInfo.user.copy(
                                id = userId,
                                username = username,
                                profilePictureUrl = profilePictureUrl
                            )
                        )
                    )
                }
            }
    }

    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote> {
        val result = chatParticipantService.getProfilePictureUploadUrl(mimeType)
        if (result is Result.Failure) return result

        val uploadUrls = (result as Result.Success).data
        val uploadResult = chatParticipantService.uploadProfilePicture(
            uploadUrl = uploadUrls.uploadUrl,
            imageBytes = imageBytes,
            headers = uploadUrls.headers
        )
        if (uploadResult is Result.Failure) return uploadResult

        return chatParticipantService.confirmProfilePictureUpload(
            publicUrl = uploadUrls.publicUrl
        ).onSuccess {
            val currentAuthInfo = sessionStorage.observeAuthInfo().first()
            sessionStorage.set(
                currentAuthInfo?.copy(
                    user = currentAuthInfo.user.copy(
                        profilePictureUrl = uploadUrls.publicUrl
                    )
                )
            )
        }
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return chatParticipantService
            .deleteProfilePicture()
            .onSuccess {
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            profilePictureUrl = null
                        )
                    )
                )
            }
    }
}