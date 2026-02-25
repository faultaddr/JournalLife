package com.pyy.journalapp.demo

import com.pyy.journalapp.core.JournalLifeCore
import com.pyy.journalapp.models.*
import com.pyy.journalapp.models.Visibility
import com.pyy.journalapp.templates.Mood
import com.pyy.journalapp.templates.Season
import com.pyy.journalapp.templates.WritingContext
import com.pyy.journalapp.utils.DateTimeUtils
import com.pyy.journalapp.utils.IdGenerator
import kotlinx.datetime.LocalDate

/**
 * JournalLifeApp 演示类
 * 展示AI智能联想、时光胶囊和情境化创作三大核心功能
 */
class JournalLifeDemo {
    private val journalLifeCore = JournalLifeCore()

    /**
     * 演示AI智能联想功能
     */
    fun demonstrateAIFeatures() {
        println("🚀 开始演示 AI 智能联想功能...")

        // 创建一个示例日记条目
        val now = DateTimeUtils.now()
        val sampleEntry = JournalEntry(
            id = IdGenerator.generateId(),
            ownerId = IdGenerator.generateId(),
            bookId = IdGenerator.generateId(),
            title = "今天的好心情",
            createdAt = now,
            updatedAt = now,
            visibility = Visibility.PUBLIC,
            tags = listOf("开心", "美好", "工作"),
            blocks = listOf(
                TextBlock(
                    id = IdGenerator.generateId(),
                    createdAt = now,
                    updatedAt = now,
                    orderIndex = 0,
                    text = "今天工作很顺利，完成了一个重要项目，同事们都很棒，感觉很开心很满足。",
                    format = TextFormat.PLAIN
                ),
                TextBlock(
                    id = IdGenerator.generateId(),
                    createdAt = now,
                    updatedAt = now,
                    orderIndex = 1,
                    text = "晚上和朋友们一起吃饭，聊了很多有趣的事情，大家都很开心。",
                    format = TextFormat.PLAIN
                )
            )
        )

        // 分析内容
        val analysis = journalLifeCore.analyzeJournalContent(sampleEntry)

        println("\n📊 AI 分析结果:")
        println("关键词: ${analysis.keywords.joinToString(", ")}")
        println("情绪: ${analysis.emotions.joinToString(", ")}")
        println("主题: ${analysis.topics.joinToString(", ")}")
        println("建议: ${analysis.suggestions.joinToString("; ")}")

        println("\n✅ AI 智能联想功能演示完成!\n")
    }

    /**
     * 演示时光胶囊功能
     */
    fun demonstrateTimeCapsuleFeatures() {
        println("🎁 开始演示 时光胶囊功能...")

        // 创建一个示例日记条目
        val now = DateTimeUtils.now()
        val entryForCapsule = JournalEntry(
            id = IdGenerator.generateId(),
            ownerId = IdGenerator.generateId(),
            bookId = IdGenerator.generateId(),
            title = "给未来自己的话",
            createdAt = now,
            updatedAt = now,
            visibility = Visibility.PRIVATE,
            tags = listOf("未来", "期望", "梦想"),
            blocks = listOf(
                TextBlock(
                    id = IdGenerator.generateId(),
                    createdAt = now,
                    updatedAt = now,
                    orderIndex = 0,
                    text = "亲爱的未来的自己，现在的我对未来充满期待，希望一年后的你能实现现在的梦想。",
                    format = TextFormat.PLAIN
                )
            )
        )

        // 创建时光胶囊，设置在30天后开启
        val targetDate = DateTimeUtils.addDays(DateTimeUtils.today(), 30)
        val timeCapsule = journalLifeCore.createTimeCapsule(entryForCapsule, targetDate)

        println("\n🕐 时光胶囊已创建:")
        println("胶囊ID: ${timeCapsule.id}")
        println("原始条目: ${timeCapsule.originalEntry.title}")
        println("目标日期: ${timeCapsule.targetDate}")
        println("创建日期: ${timeCapsule.creationDate}")
        println("状态: ${timeCapsule.status}")

        // 检查即将到期的时光胶囊
        val upcomingCapsules = journalLifeCore.getUpcomingTimeCapsules(listOf(timeCapsule), 45)
        println("\n🔔 即将到期的时光胶囊数量: ${upcomingCapsules.size}")

        println("\n✅ 时光胶囊功能演示完成!\n")
    }

    /**
     * 演示情境化创作功能
     */
    fun demonstrateContextualWritingFeatures() {
        println("📝 开始演示 情境化创作功能...")

        // 不同的情境
        val travelContext = WritingContext(
            location = "Paris, France",
            weather = "Sunny",
            mood = Mood.EXCITED,
            isTraveling = true,
            season = Season.SUMMER
        )

        val healthContext = WritingContext(
            location = "Home",
            mood = Mood.CALM,
            eventType = com.pyy.journalapp.templates.EventType.HEALTH_CHECKUP
        )

        val birthdayContext = WritingContext(
            mood = Mood.HAPPY,
            eventType = com.pyy.journalapp.templates.EventType.BIRTHDAY
        )

        // 推荐模板
        val travelTemplate = journalLifeCore.recommendTemplate(travelContext)
        val healthTemplate = journalLifeCore.recommendTemplate(healthContext)
        val birthdayTemplate = journalLifeCore.recommendTemplate(birthdayContext)

        println("\n🧩 情境化模板推荐:")
        println("旅行情境 -> 模板: ${travelTemplate.name}")
        println("健康检查 -> 模板: ${healthTemplate.name}")
        println("生日庆祝 -> 模板: ${birthdayTemplate.name}")

        // 创建一个基础日记条目
        val now = DateTimeUtils.now()
        val baseEntry = JournalEntry(
            id = IdGenerator.generateId(),
            ownerId = IdGenerator.generateId(),
            bookId = IdGenerator.generateId(),
            title = "情境化日记",
            createdAt = now,
            updatedAt = now,
            visibility = Visibility.PUBLIC,
            blocks = listOf(
                TextBlock(
                    id = IdGenerator.generateId(),
                    createdAt = now,
                    updatedAt = now,
                    orderIndex = 0,
                    text = "今天是特殊的一天。",
                    format = TextFormat.PLAIN
                )
            )
        )

        // 应用旅行模板到日记条目
        val enhancedEntry = journalLifeCore.applyTemplateToEntry(baseEntry, travelContext)
        println("\n✨ 应用模板后的日记条目块数量: ${enhancedEntry.blocks.size}")

        println("\n✅ 情境化创作功能演示完成!\n")
    }

    /**
     * 演示三大功能的综合应用
     */
    fun demonstrateIntegratedFeatures() {
        println("🎯 开始演示 三大功能综合应用...")

        // 创建一个旅行日记条目
        val now = DateTimeUtils.now()
        val travelEntry = JournalEntry(
            id = IdGenerator.generateId(),
            ownerId = IdGenerator.generateId(),
            bookId = IdGenerator.generateId(),
            title = "巴黎浪漫之旅",
            createdAt = now,
            updatedAt = now,
            visibility = Visibility.PUBLIC,
            tags = listOf("旅行", "欧洲", "浪漫"),
            blocks = listOf(
                TextBlock(
                    id = IdGenerator.generateId(),
                    createdAt = now,
                    updatedAt = now,
                    orderIndex = 0,
                    text = "今天在巴黎的塞纳河畔漫步，感受到了这座城市的浪漫气息。",
                    format = TextFormat.PLAIN
                )
            )
        )

        // 情境：正在旅行
        val travelContext = WritingContext(
            location = "Paris, France",
            weather = "Sunny with light breeze",
            mood = Mood.EXCITED,
            isTraveling = true,
            season = Season.SPRING
        )

        // 1. AI分析
        val analysis = journalLifeCore.analyzeJournalContent(travelEntry)
        println("\n🔍 AI 智能联想分析:")
        println("   关键词: ${analysis.keywords.take(3).joinToString(", ")}")
        println("   情绪: ${analysis.emotions.joinToString(", ")}")

        // 2. 模板推荐
        val recommendedTemplate = journalLifeCore.recommendTemplate(travelContext)
        println("\n📋 情境化创作模板: ${recommendedTemplate.name}")
        println("   描述: ${recommendedTemplate.description}")

        // 3. 综合洞察
        val insights = journalLifeCore.intelligentJournalAssistant(travelEntry, travelContext)
        println("\n💡 综合智能洞察:")
        println("   AI建议: ${insights.suggestions.take(2).joinToString("; ")}")
        println("   推荐模板: ${insights.recommendedTemplate.name}")

        // 4. 创建时光胶囊
        val targetDate = DateTimeUtils.addDays(DateTimeUtils.today(), 365)
        val timeCapsule = journalLifeCore.createTimeCapsule(travelEntry, targetDate)
        println("\n🎁 已创建时光胶囊，将在一年后开启: ${timeCapsule.targetDate}")

        println("\n✅ 三大功能综合应用演示完成!\n")
    }

    /**
     * 运行完整演示
     */
    fun runFullDemo() {
        println("🌟 欢迎使用 JournalLifeApp 功能演示!")
        println("=========================================")

        demonstrateAIFeatures()
        demonstrateTimeCapsuleFeatures()
        demonstrateContextualWritingFeatures()
        demonstrateIntegratedFeatures()

        println("🎊 JournalLifeApp 演示结束! 🎊")
        println("\nJournalLifeApp 三大核心功能:")
        println("1. AI智能联想 - 自动分析内容并提供智能建议")
        println("2. 时光胶囊 - 封存记忆，传递给未来的自己")
        println("3. 情境化创作 - 基于情境的智能模板推荐")
        println("\n这些功能相互配合，为您提供智能化的日记体验！")
    }
}

/**
 * 主函数，运行演示
 */
fun main() {
    val demo = JournalLifeDemo()
    demo.runFullDemo()
}
