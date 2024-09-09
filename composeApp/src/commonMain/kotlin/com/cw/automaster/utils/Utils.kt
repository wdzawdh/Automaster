package com.cw.automaster.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun getCurrentTimestamp(): Long {
    val currentInstant: Instant = Clock.System.now()
    return currentInstant.toEpochMilliseconds()
}