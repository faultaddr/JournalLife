package com.pyy.journalapp.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/**
 * 日期时间工具类
 * 提供跨平台兼容的日期时间功能
 */
object DateTimeUtils {

    /**
     * 获取当前时间
     */
    fun now(): LocalDateTime {
        return getCurrentDateTime()
    }

    /**
     * 获取当前日期
     */
    fun today(): LocalDate {
        return now().date
    }

    /**
     * 获取当前时间戳（毫秒）
     */
    fun currentTimeMillis(): Long {
        return getCurrentTimeMillis()
    }

    /**
     * 计算两个日期之间的天数差
     */
    fun daysBetween(start: LocalDate, end: LocalDate): Int {
        // 简化的天数计算实现
        var days = 0
        var current = start

        if (start < end) {
            while (current < end) {
                days++
                current = current.nextDay()
            }
        } else if (start > end) {
            while (current > end) {
                days--
                current = current.previousDay()
            }
        }

        return days
    }

    /**
     * 添加天数到日期
     */
    fun addDays(date: LocalDate, daysToAdd: Int): LocalDate {
        var result = date
        if (daysToAdd >= 0) {
            repeat(daysToAdd) {
                result = result.nextDay()
            }
        } else {
            repeat(-daysToAdd) {
                result = result.previousDay()
            }
        }
        return result
    }

    /**
     * LocalDate 扩展：获取下一天
     */
    private fun LocalDate.nextDay(): LocalDate {
        val nextDay = this.dayOfMonth + 1

        return if (nextDay > daysInMonth(this.year, this.monthNumber)) {
            if (this.monthNumber == 12) {
                LocalDate(this.year + 1, 1, 1)
            } else {
                LocalDate(this.year, this.monthNumber + 1, 1)
            }
        } else {
            LocalDate(this.year, this.month, nextDay)
        }
    }

    /**
     * LocalDate 扩展：获取前一天
     */
    private fun LocalDate.previousDay(): LocalDate {
        val prevDay = this.dayOfMonth - 1

        return if (prevDay < 1) {
            if (this.monthNumber == 1) {
                val prevMonthDays = daysInMonth(this.year - 1, 12)
                LocalDate(this.year - 1, 12, prevMonthDays)
            } else {
                val prevMonthDays = daysInMonth(this.year, this.monthNumber - 1)
                LocalDate(this.year, this.monthNumber - 1, prevMonthDays)
            }
        } else {
            LocalDate(this.year, this.month, prevDay)
        }
    }

    /**
     * 获取某年某月的天数
     */
    private fun daysInMonth(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
    }

    /**
     * 判断是否是闰年
     */
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}

/**
 * 平台特定的当前日期时间获取函数
 */
internal expect fun getCurrentDateTime(): LocalDateTime

/**
 * 平台特定的当前时间戳获取函数
 */
internal expect fun getCurrentTimeMillis(): Long
