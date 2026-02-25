package com.pyy.journalapp.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import platform.posix.gettimeofday
import platform.posix.timeval

/**
 * iOS 平台的当前日期时间获取实现
 */
@OptIn(ExperimentalTime::class)
internal actual fun getCurrentDateTime(): LocalDateTime {
    val millis = getCurrentTimeMillis()
    return Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())
}

/**
 * iOS 平台的当前时间戳获取实现
 */
@OptIn(ExperimentalForeignApi::class)
internal actual fun getCurrentTimeMillis(): Long {
    return memScoped {
        val tv = alloc<timeval>()
        gettimeofday(tv.ptr, null)
        tv.tv_sec * 1000 + tv.tv_usec / 1000
    }
}
