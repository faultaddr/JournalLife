package com.pyy.journalapp.utils

import kotlin.random.Random

/**
 * ID生成器工具类
 */
object IdGenerator {
    /**
     * 生成唯一的ID
     */
    fun generateId(): String {
        val timestamp = DateTimeUtils.currentTimeMillis()
        val randomPart = Random.nextLong(1000000)
        return "${timestamp}_${randomPart}"
    }
}
