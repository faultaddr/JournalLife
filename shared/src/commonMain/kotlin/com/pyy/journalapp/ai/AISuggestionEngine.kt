package com.pyy.journalapp.ai

import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.models.Block
import com.pyy.journalapp.models.TextBlock
import com.pyy.journalapp.models.ImageBlock
import com.pyy.journalapp.models.HeadingBlock
import com.pyy.journalapp.models.QuoteBlock
import com.pyy.journalapp.models.TodoBlock
import com.pyy.journalapp.models.DividerBlock

/**
 * AI智能联想到引擎，负责分析内容并生成智能标签和建议
 */
class AISuggestionEngine {

    /**
     * 分析日记条目的内容并生成标签
     */
    fun analyzeContent(entry: JournalEntry): ContentAnalysis {
        val textBlocks = entry.blocks.filterIsInstance<TextBlock>()
        val imageBlocks = entry.blocks.filterIsInstance<ImageBlock>()

        val textContent = textBlocks.joinToString(" ") { it.text }
        val detectedKeywords = detectKeywords(textContent)
        val detectedEmotions = detectEmotions(textContent)
        val relatedTopics = detectRelatedTopics(textContent)

        return ContentAnalysis(
            keywords = detectedKeywords,
            emotions = detectedEmotions,
            topics = relatedTopics,
            imageTags = extractImageTags(imageBlocks),
            suggestions = generateSuggestions(entry)
        )
    }

    /**
     * 检测关键词
     */
    private fun detectKeywords(content: String): List<String> {
        // 简单关键词检测 - 在真实应用中这里会有更复杂的AI逻辑
        val keywords = mutableListOf<String>()
        val commonKeywords = listOf(
            "开心", "快乐", "幸福", "难过", "悲伤", "工作", "生活", "学习",
            "朋友", "家人", "旅行", "美食", "运动", "音乐", "电影", "读书"
        )

        for (keyword in commonKeywords) {
            if (content.contains(keyword)) {
                keywords.add(keyword)
            }
        }

        // 添加提取的名词（简化版）
        val nouns = extractNouns(content)
        keywords.addAll(nouns.take(5)) // 最多取5个名词

        return keywords.distinct()
    }

    /**
     * 检测情绪
     */
    private fun detectEmotions(content: String): List<String> {
        val emotionKeywords = mapOf(
            "positive" to listOf("开心", "快乐", "幸福", "兴奋", "满足", "喜欢", "爱", "美好", "愉快", "舒服"),
            "negative" to listOf("难过", "悲伤", "沮丧", "痛苦", "愤怒", "讨厌", "烦", "愁", "累", "失望"),
            "calm" to listOf("平静", "安宁", "放松", "舒适", "自在", "宁静", "淡然", "悠然")
        )

        val detectedEmotions = mutableListOf<String>()

        for ((emotion, words) in emotionKeywords) {
            for (word in words) {
                if (content.contains(word)) {
                    if (!detectedEmotions.contains(emotion)) {
                        detectedEmotions.add(emotion)
                        break
                    }
                }
            }
        }

        return if (detectedEmotions.isEmpty()) listOf("neutral") else detectedEmotions
    }

    /**
     * 检测相关话题
     */
    private fun detectRelatedTopics(content: String): List<String> {
        val topicKeywords = mapOf(
            "工作职场" to listOf("工作", "上班", "会议", "项目", "同事", "老板", "任务", "职业"),
            "健康生活" to listOf("健康", "运动", "锻炼", "健身", "睡眠", "饮食", "养生", "瑜伽"),
            "美食烹饪" to listOf("食物", "菜", "餐厅", "吃", "做饭", "美食", "烹饪", "料理"),
            "学习成长" to listOf("学习", "课程", "考试", "知识", "技能", "进步", "成长", "教育"),
            "休闲娱乐" to listOf("电影", "音乐", "游戏", "旅游", "休闲", "娱乐", "爱好", "兴趣"),
            "情感关系" to listOf("爱情", "友情", "家庭", "亲情", "感情", "恋爱", "伴侣", "关系")
        )

        val detectedTopics = mutableListOf<String>()

        for ((topic, keywords) in topicKeywords) {
            for (keyword in keywords) {
                if (content.contains(keyword)) {
                    if (!detectedTopics.contains(topic)) {
                        detectedTopics.add(topic)
                        break
                    }
                }
            }
        }

        return detectedTopics
    }

    /**
     * 提取名词（简化版）
     */
    private fun extractNouns(content: String): List<String> {
        // 这简化版本，实际应用中需要使用自然语言处理
        val words = content.split("[\\s，。！？；：、]+".toRegex())
        val chinesePattern = Regex("[\\u4e00-\\u9fa5]+")

        return words.filter { word ->
            word.length >= 2 && word.matches(chinesePattern) && !isStopWord(word)
        }
    }

    /**
     * 判断是否为停用词
     */
    private fun isStopWord(word: String): Boolean {
        val stopWords = setOf("的", "了", "在", "是", "我", "有", "和", "就", "不", "人", "都", "一", "一个", "这个", "那个")
        return stopWords.contains(word)
    }

    /**
     * 提取图片标签
     */
    private fun extractImageTags(images: List<ImageBlock>): List<String> {
        // 在真实应用中，这里会分析图片内容
        return images.map { "image_${it.imageId.hashCode()}" }
    }

    /**
     * 生成建议
     */
    private fun generateSuggestions(entry: JournalEntry): List<String> {
        val suggestions = mutableListOf<String>()

        if (entry.title.contains("计划") || entry.title.contains("目标")) {
            suggestions.add("💡 考虑制定详细执行步骤")
        }

        if (entry.blocks.filterIsInstance<TodoBlock>().count() > 3) {
            suggestions.add("✅ 检查已完成的任务清单")
        }

        if (entry.blocks.filterIsInstance<ImageBlock>().size > 0) {
            suggestions.add("🖼️ 添加图片描述或感受")
        }

        if (entry.blocks.count() > 10) {
            suggestions.add("📝 考虑添加小结或反思")
        }

        if (entry.title.isEmpty() && entry.blocks.filterIsInstance<HeadingBlock>().isEmpty()) {
            suggestions.add("🖊️ 添加标题或小节标题")
        }

        return suggestions
    }
}

/**
 * 内容分析结果
 */
data class ContentAnalysis(
    val keywords: List<String>,
    val emotions: List<String>,
    val topics: List<String>,
    val imageTags: List<String>,
    val suggestions: List<String>
)