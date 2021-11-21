package com.adwi.core.util

import timber.log.Timber

class Logger(
    private val tag: String = "",
    private val isDebug: Boolean = true,
) {
    fun log(msg: String) {
        if (!isDebug) {
            // production logging - Crashlytics or whatever you want to use
        } else {
            Timber.tag(tag).d(msg)
        }
    }

    companion object Factory {
        fun buildDebug(className: String): Logger {
            return Logger(
                tag = className,
                isDebug = true,
            )
        }

        fun buildRelease(className: String): Logger {
            return Logger(
                tag = className,
                isDebug = false,
            )
        }
    }
}