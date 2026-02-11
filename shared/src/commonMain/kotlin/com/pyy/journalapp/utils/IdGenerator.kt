package com.pyy.journalapp.utils

import kotlinx.datetime.Clock
import kotlin.random.Random

/**
 * ID生成器工具类
 */
object IdGenerator {
    /**
     * 生成唯一的ID
     */
    fun generateId(): String {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        val randomPart = Random.nextLong(1000000)
        return "${timestamp}_${randomPart}"
    }
}