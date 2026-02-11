package com.pyy.journalapp.templates

import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.models.Block
import com.pyy.journalapp.models.TextBlock
import com.pyy.journalapp.models.ImageBlock
import com.pyy.journalapp.models.HeadingBlock
import com.pyy.journalapp.models.QuoteBlock
import com.pyy.journalapp.models.TodoBlock
import com.pyy.journalapp.models.DividerBlock
import com.pyy.journalapp.models.TextFormat
import com.pyy.journalapp.utils.IdGenerator
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * 情境化創作模板管理器
 */
@OptIn(ExperimentalStdlibApi::class, ExperimentalTime::class)
class TemplateManager {

    /**
     * 根據情境推薦模板
     */
    fun recommendTemplate(context: WritingContext, content: String = ""): JournalTemplate {
        val detectedSituation = detectSituation(context, content)

        return when (detectedSituation) {
            Situation.TRAVEL -> TravelTemplate()
            Situation.BIRTHDAY -> BirthdayTemplate()
            Situation.RELATIONSHIP -> RelationshipTemplate()
            Situation.HEALTH -> HealthTemplate()
            Situation.WORK -> WorkTemplate()
            Situation.LEARNING -> LearningTemplate()
            Situation.EMOTIONAL -> EmotionalSupportTemplate()
            Situation.EVENT -> EventTemplate()
            else -> DefaultTemplate()
        }
    }

    /**
     * 檢測寫作情境
     */
    private fun detectSituation(context: WritingContext, content: String): Situation {
        // 根據上下文檢測情境
        val locationKeywords = listOf("景點", "酒店", "風景", "城市", "旅行", "旅遊", "景區", "旅途", "路上")
        val birthdayKeywords = listOf("生日", "週年", "紀念日", "慶祝", "禮物", "蛋糕", "聚會")
        val healthKeywords = listOf("身體", "健康", "體檢", "運動", "鍛煉", "睡眠", "飲食", "藥物", "治療")
        val workKeywords = listOf("工作", "項目", "會議", "匯報", "任務", "加班", "公司", "老闆", "同事")
        val learningKeywords = listOf("學習", "課程", "考試", "作業", "讀書", "筆記", "知識", "技能")

        // 檢查內容中的關鍵詞
        if (content.isNotEmpty()) {
            if (locationKeywords.any { content.contains(it) }) return Situation.TRAVEL
            if (birthdayKeywords.any { content.contains(it) }) return Situation.BIRTHDAY
            if (healthKeywords.any { content.contains(it) }) return Situation.HEALTH
            if (workKeywords.any { content.contains(it) }) return Situation.WORK
            if (learningKeywords.any { content.contains(it) }) return Situation.LEARNING
        }

        // 檢查上下文中的情境
        return when {
            context.isTraveling -> Situation.TRAVEL
            context.eventType == EventType.BIRTHDAY -> Situation.BIRTHDAY
            context.eventType == EventType.ANNIVERSARY -> Situation.BIRTHDAY
            context.mood == Mood.HAPPY -> Situation.BIRTHDAY // Happy mood might indicate celebration
            context.mood == Mood.SAD || context.mood == Mood.ANXIOUS -> Situation.EMOTIONAL
            context.location != null && context.location.contains("醫院") -> Situation.HEALTH
            context.eventType == EventType.EVENT -> Situation.EVENT
            else -> Situation.DEFAULT
        }
    }

    /**
     * 為現有條目應用模板
     */
    fun applyTemplateToEntry(template: JournalTemplate, entry: JournalEntry): JournalEntry {
        // 使用模板的建議來增強現有的條目
        val enhancedBlocks = template.suggestedBlocks.toMutableList()

        // 如果原條目有內容，則合併
        if (entry.blocks.isNotEmpty()) {
            enhancedBlocks.addAll(entry.blocks)
        }

        return entry.copy(
            blocks = enhancedBlocks
        )
    }
}

/**
 * 寫作情境數據類
 */
data class WritingContext(
    val location: String? = null,
    val weather: String? = null,
    val mood: Mood? = null,
    val eventType: EventType? = null,
    val isTraveling: Boolean = false,
    val season: Season? = null
)

/**
 * 情緒枚舉
 */
enum class Mood {
    HAPPY, SAD, ANXIOUS, EXCITED, CALM, ANGRY, CONFUSED, GRATEFUL
}

/**
 * 事件類型
 */
enum class EventType {
    BIRTHDAY, ANNIVERSARY, TRAVEL, MEETING, WORK_EVENT, LEARNING, HEALTH_CHECKUP, EVENT
}

/**
 * 季節
 */
enum class Season {
    SPRING, SUMMER, AUTUMN, WINTER
}

/**
 * 情境類型
 */
enum class Situation {
    TRAVEL, BIRTHDAY, RELATIONSHIP, HEALTH, WORK, LEARNING, EMOTIONAL, EVENT, DEFAULT
}

/**
 * 模板基類
 */
abstract class JournalTemplate(
    val name: String,
    val description: String,
    val suggestedBlocks: List<Block>,
    val suggestedLayout: LayoutType = LayoutType.STANDARD
) {
    abstract fun generateDefaultContent(): List<Block>
}

/**
 * 布局類型
 */
enum class LayoutType {
    STANDARD, GRID, COLUMN, TIMELINE, COLLAGE
}

/**
 * 旅行模板
 */
class TravelTemplate : JournalTemplate(
    name = "旅行日記模板",
    description = "為旅行記錄設計的模板，包含景點、美食、住宿等板塊",
    suggestedBlocks = generateTravelTemplateBlocks()
) {
    override fun generateDefaultContent(): List<Block> = suggestedBlocks
}

/**
 * 生日模板
 */
class BirthdayTemplate : JournalTemplate(
    name = "生日慶典模板",
    description = "為生日、紀念日等特殊日子設計的模板",
    suggestedBlocks = generateBirthdayTemplateBlocks()
) {
    override fun generateDefaultContent(): List<Block> = suggestedBlocks
}

/**
 * 情感支持模板
 */
class EmotionalSupportTemplate : JournalTemplate(
    name = "情感宣泄模板",
    description = "幫助用戶表達和處理情感的模板",
    suggestedBlocks = generateEmotionalTemplateBlocks()
) {
    override fun generateDefaultContent(): List<Block> = suggestedBlocks
}

/**
 * 默認模板
 */
class DefaultTemplate : JournalTemplate(
    name = "標準模板",
    description = "通用的標準日記模板",
    suggestedBlocks = generateDefaultTemplateBlocks()
) {
    override fun generateDefaultContent(): List<Block> = suggestedBlocks
}

/**
 * 其他模板類（略簡）
 */
class RelationshipTemplate : JournalTemplate(
    name = "關係記錄模板",
    description = "記錄人際關係的模板",
    suggestedBlocks = emptyList()
) {
    override fun generateDefaultContent(): List<Block> = listOf()
}

class HealthTemplate : JournalTemplate(
    name = "健康記錄模板",
    description = "跟蹤健康狀況的模板",
    suggestedBlocks = emptyList()
) {
    override fun generateDefaultContent(): List<Block> = listOf()
}

class WorkTemplate : JournalTemplate(
    name = "工作記錄模板",
    description = "記錄工作事務的模板",
    suggestedBlocks = emptyList()
) {
    override fun generateDefaultContent(): List<Block> = listOf()
}

class LearningTemplate : JournalTemplate(
    name = "學習記錄模板",
    description = "記錄學習進展的模板",
    suggestedBlocks = emptyList()
) {
    override fun generateDefaultContent(): List<Block> = listOf()
}

class EventTemplate : JournalTemplate(
    name = "活動記錄模板",
    description = "記錄特殊事件的模板",
    suggestedBlocks = emptyList()
) {
    override fun generateDefaultContent(): List<Block> = listOf()
}

// 生成函數
private fun generateTravelTemplateBlocks(): List<Block> = listOf(
    @OptIn(ExperimentalTime::class)
    HeadingBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 0,
        text = "今日行程",
        level = 1
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 1,
        text = "今天的景點是：",
        format = TextFormat.PLAIN
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 2,
        text = "今日感受：",
        format = TextFormat.PLAIN
    ),
    @OptIn(ExperimentalTime::class)
    DividerBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 3
    ),
    @OptIn(ExperimentalTime::class)
    HeadingBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 4,
        text = "美食記錄",
        level = 2
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 5,
        text = "今天吃到的美食：",
        format = TextFormat.PLAIN
    )
)

private fun generateBirthdayTemplateBlocks(): List<Block> = listOf(
    @OptIn(ExperimentalTime::class)
    HeadingBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 0,
        text = "🎉 今天是特別的日子！",
        level = 1
    ),
    @OptIn(ExperimentalTime::class)
    QuoteBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 1,
        text = "願歲月不老，友誼長存",
        author = "未知"
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 2,
        text = "今年的生日願望是：",
        format = TextFormat.PLAIN
    ),
    @OptIn(ExperimentalTime::class)
    TodoBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 3,
        text = "慶祝活動",
        completed = false
    ),
    @OptIn(ExperimentalTime::class)
    TodoBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 4,
        text = "感謝名單",
        completed = false
    )
)

private fun generateEmotionalTemplateBlocks(): List<Block> = listOf(
    @OptIn(ExperimentalTime::class)
    HeadingBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 0,
        text = "情感釋放空間",
        level = 1
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 1,
        text = "此刻我的感受：",
        format = TextFormat.PLAIN
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 2,
        text = "導致這種感受的原因：",
        format = TextFormat.PLAIN
    ),
    @OptIn(ExperimentalTime::class)
    DividerBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 3
    ),
    @OptIn(ExperimentalTime::class)
    HeadingBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 4,
        text = "積極展望",
        level = 2
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 5,
        text = "明天我希望：",
        format = TextFormat.PLAIN
    )
)

private fun generateDefaultTemplateBlocks(): List<Block> = listOf(
    @OptIn(ExperimentalTime::class)
    HeadingBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 0,
        text = "今日記錄",
        level = 1
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 1,
        text = "今天發生了：",
        format = TextFormat.PLAIN
    ),
    @OptIn(ExperimentalTime::class)
    TextBlock(
        id = IdGenerator.generateId(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        orderIndex = 2,
        text = "我的感受：",
        format = TextFormat.PLAIN
    )
)