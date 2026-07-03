package com.example.core.presentation.ext

import chirpappkmp.core.presentation.generated.resources.Res
import chirpappkmp.core.presentation.generated.resources.error_bad_request
import chirpappkmp.core.presentation.generated.resources.error_conflict
import chirpappkmp.core.presentation.generated.resources.error_disk_full
import chirpappkmp.core.presentation.generated.resources.error_forbidden
import chirpappkmp.core.presentation.generated.resources.error_no_internet
import chirpappkmp.core.presentation.generated.resources.error_not_found
import chirpappkmp.core.presentation.generated.resources.error_payload_too_large
import chirpappkmp.core.presentation.generated.resources.error_request_timeout
import chirpappkmp.core.presentation.generated.resources.error_serialization
import chirpappkmp.core.presentation.generated.resources.error_server
import chirpappkmp.core.presentation.generated.resources.error_service_unavailable
import chirpappkmp.core.presentation.generated.resources.error_too_many_requests
import chirpappkmp.core.presentation.generated.resources.error_unauthorized
import chirpappkmp.core.presentation.generated.resources.error_unknown
import chirpappkmp.core.presentation.generated.resources.unable_to_send_message
import com.example.core.domain.util.DataError
import com.example.core.presentation.util.UiText

fun DataError.toUiText(): UiText {
    val resource = when (this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.NOT_FOUND -> Res.string.error_not_found
        DataError.Remote.BAD_REQUEST -> Res.string.error_bad_request
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.UNAUTHORIZED -> Res.string.error_unauthorized
        DataError.Remote.FORBIDDEN -> Res.string.error_forbidden
        DataError.Remote.NOT_FOUND -> Res.string.error_not_found
        DataError.Remote.CONFLICT -> Res.string.error_conflict
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
        DataError.Remote.SERVER_ERROR -> Res.string.error_server
        DataError.Remote.SERVICE_UNAVAILABLE -> Res.string.error_service_unavailable
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Local.UNKNOWN, DataError.Remote.UNKNOWN -> Res.string.error_unknown
        DataError.Connection.NOT_CONNECTED -> Res.string.error_no_internet
        DataError.Connection.MESSAGE_SEND_FAILED -> Res.string.unable_to_send_message
    }

    return UiText.Resource(resource)
}