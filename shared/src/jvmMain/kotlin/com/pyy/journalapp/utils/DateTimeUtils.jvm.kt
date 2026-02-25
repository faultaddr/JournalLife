package com.pyy.journalapp.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * JVM 平台的当前日期时间获取实现
 */
@OptIn(ExperimentalTime::class)
internal actual fun getCurrentDateTime(): LocalDateTime {
    val millis = System.currentTimeMillis()
    return Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())
}

/**
 * JVM 平台的当前时间戳获取实现
 */
internal actual fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}
