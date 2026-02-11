package com.pyy.journalapp.core

import com.pyy.journalapp.ai.AISuggestionEngine
import com.pyy.journalapp.ai.ContentAnalysis
import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.timemachine.TimeCapsuleManager
import com.pyy.journalapp.timemachine.TimeCapsule
import com.pyy.journalapp.templates.TemplateManager
import com.pyy.journalapp.templates.WritingContext
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

/**
 * JournalLifeApp核心整合类
 * 将AI智能联想、时光胶囊和情境化创作三大功能整合在一起
 */
@OptIn(ExperimentalTime::class)
class JournalLifeCore {
    private val aiSuggestionEngine = AISuggestionEngine()
    private val timeCapsuleManager = TimeCapsuleManager()
    private val templateManager = TemplateManager()

    /**
     * 分析日记内容并提供智能建议
     */
    fun analyzeJournalContent(entry: JournalEntry): ContentAnalysis {
        return aiSuggestionEngine.analyzeContent(entry)
    }

    /**
     * 根据写作情境推荐合适的模板
     */
    fun recommendTemplate(context: WritingContext, content: String = "") =
        templateManager.recommendTemplate(context, content)

    /**
     * 创建时光胶囊
     */
    fun createTimeCapsule(entry: JournalEntry, targetDate: LocalDate): TimeCapsule {
        return timeCapsuleManager.createCapsule(entry, targetDate)
    }

    /**
     * 检查到期的时光胶囊
     */
    fun checkDueTimeCapsules(capsules: List<TimeCapsule>, currentDate: LocalDate) =
        timeCapsuleManager.checkDueCapsules(capsules, currentDate)

    /**
     * 获取即将到来的时光胶囊（未来几天内到期）
     */
    fun getUpcomingTimeCapsules(capsules: List<TimeCapsule>, daysAhead: Int = 7) =
        timeCapsuleManager.getUpcomingCapsules(capsules, daysAhead)

    /**
     * 获取今日的纪念日（来自过去同月同日的日记）
     */
    fun getAnniversaryEntries(entries: List<JournalEntry>, currentDate: LocalDate) =
        timeCapsuleManager.getAnniversaryEntries(entries, currentDate)

    /**
     * 应用模板到现有日记条目
     */
    fun applyTemplateToEntry(entry: JournalEntry, context: WritingContext): JournalEntry {
        val template = templateManager.recommendTemplate(context)
        return templateManager.applyTemplateToEntry(template, entry)
    }

    /**
     * 智能日记助手 - 综合分析日记内容、提供模板建议和时光胶囊选项
     */
    fun intelligentJournalAssistant(entry: JournalEntry, context: WritingContext): JournalInsights {
        val contentAnalysis = analyzeJournalContent(entry)
        val recommendedTemplate = recommendTemplate(context, entry.title + " " +
            entry.blocks.joinToString(" ") { block ->
                when(block) {
                    is com.pyy.journalapp.models.TextBlock -> block.text
                    else -> ""
                }
            })

        return JournalInsights(
            contentAnalysis = contentAnalysis,
            recommendedTemplate = recommendedTemplate,
            suggestions = contentAnalysis.suggestions
        )
    }
}

/**
 * 日记洞察数据类
 */
data class JournalInsights(
    val contentAnalysis: ContentAnalysis,
    val recommendedTemplate: com.pyy.journalapp.templates.JournalTemplate,
    val suggestions: List<String>
)