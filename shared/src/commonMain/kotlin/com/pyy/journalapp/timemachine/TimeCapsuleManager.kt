package com.pyy.journalapp.timemachine

import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.utils.DateTimeUtils
import com.pyy.journalapp.utils.IdGenerator
import kotlinx.datetime.LocalDate
import kotlin.random.Random

/**
 * 时光胶囊功能类，用于定时发送过去的内容到未来
 */
class TimeCapsuleManager {

    /**
     * 创建一个时光胶囊，将日记条目发送到未来
     * @param entry 要封存的日记条目
     * @param targetDate 目标日期，到这一天时会收到这条记录
     */
    fun createCapsule(entry: JournalEntry, targetDate: LocalDate): TimeCapsule {
        return TimeCapsule(
            id = IdGenerator.generateId(),
            originalEntry = entry,
            targetDate = targetDate,
            creationDate = DateTimeUtils.today(),
            status = CapsuleStatus.ACTIVE
        )
    }

    /**
     * 检查是否有到达目标日期的时光胶囊
     * @param currentDate 当前日期
     * @return 到期的时光胶囊列表
     */
    fun checkDueCapsules(capsules: List<TimeCapsule>, currentDate: LocalDate): List<TimeCapsule> {
        return capsules.filter { capsule ->
            capsule.status == CapsuleStatus.ACTIVE &&
            (capsule.targetDate <= currentDate)
        }
    }

    /**
     * 生成时光胶囊ID
     */
    private fun generateCapsuleId(): String {
        return "capsule_${getTimeMillis()}_${Random.nextInt(1000)}"
    }

    /**
     * 获取当前时间戳
     */
    private fun getTimeMillis(): Long {
        return DateTimeUtils.currentTimeMillis()
    }

    /**
     * 获取即将到期的时光胶囊
     */
    fun getUpcomingCapsules(capsules: List<TimeCapsule>, daysAhead: Int = 7): List<TimeCapsule> {
        val currentDate = DateTimeUtils.today()
        val targetDate = DateTimeUtils.addDays(currentDate, daysAhead)

        return capsules.filter { capsule ->
            capsule.status == CapsuleStatus.ACTIVE &&
            capsule.targetDate > currentDate &&
            capsule.targetDate <= targetDate
        }.sortedBy { it.targetDate }
    }

    /**
     * 添加天数到日期
     */
    private fun addDaysToDate(date: LocalDate, daysToAdd: Int): LocalDate {
        return DateTimeUtils.addDays(date, daysToAdd)
    }

    /**
     * 获取今天的纪念日（来自去年、前年等）
     */
    fun getAnniversaryEntries(entries: List<JournalEntry>, currentDate: LocalDate): List<AnniversaryEntry> {
        val anniversaryEntries = mutableListOf<AnniversaryEntry>()

        for (entry in entries) {
            val entryDate = entry.createdAt.date
            // 检查月份和日期是否相同，但年份不同
            if (entryDate.monthNumber == currentDate.monthNumber &&
                entryDate.dayOfMonth == currentDate.dayOfMonth &&
                entryDate.year < currentDate.year) {

                val yearsAgo = currentDate.year - entryDate.year
                anniversaryEntries.add(
                    AnniversaryEntry(
                        originalEntry = entry,
                        yearsAgo = yearsAgo,
                        anniversaryDate = currentDate
                    )
                )
            }
        }

        return anniversaryEntries
    }

    /**
     * 计算距离时光胶囊到期的天数
     */
    fun daysUntilCapsule(capsule: TimeCapsule): Int {
        val currentDate = DateTimeUtils.today()

        // 如果目标日期小于当前日期，则返回0
        if (capsule.targetDate <= currentDate) {
            return 0
        }

        // 使用 DateTimeUtils 计算天数差
        return DateTimeUtils.daysBetween(currentDate, capsule.targetDate)
    }
}

/**
 * 时光胶囊数据类
 */
data class TimeCapsule(
    val id: String,
    val originalEntry: JournalEntry,
    val targetDate: LocalDate,
    val creationDate: LocalDate,
    val status: CapsuleStatus
)

/**
 * 时光胶囊状态
 */
enum class CapsuleStatus {
    ACTIVE,      // 激活状态，等待发送
    DELIVERED,   // 已送达
    CANCELLED    // 已取消
}

/**
 * 纪念日条目
 */
data class AnniversaryEntry(
    val originalEntry: JournalEntry,
    val yearsAgo: Int,
    val anniversaryDate: LocalDate
)
