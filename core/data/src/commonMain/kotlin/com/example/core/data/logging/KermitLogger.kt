package com.example.core.data.logging

import co.touchlab.kermit.Logger
import com.example.core.domain.logging.ChirpLogger

object KermitLogger : ChirpLogger {
    override fun info(message: String) {
        Logger.i(message)
    }

    override fun warning(message: String) {
        Logger.w(message)
    }

    override fun debug(message: String) {
        Logger.d(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        Logger.e(message, throwable)
    }

}