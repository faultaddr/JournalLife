package com.pyy.journalapp.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyy.journalapp.core.JournalLifeCore
import com.pyy.journalapp.models.*
import com.pyy.journalapp.models.Visibility
import com.pyy.journalapp.templates.Mood
import com.pyy.journalapp.templates.Season
import com.pyy.journalapp.templates.WritingContext
import kotlinx.datetime.*

/**
 * JournalLifeApp 主界面
 * 展示AI智能联想、时光胶囊和情境化创作三大核心功能
 */
@Composable
fun JournalLifeAppMain() {
    MaterialTheme {
        val journalLifeCore = remember { JournalLifeCore() }

        // 主界面状态管理
        var currentSection by remember { mutableStateOf(AppSection.Home) }
        var journalEntries by remember { mutableStateOf(emptyList<JournalEntry>()) }
        var timeCapsules by remember { mutableStateOf(emptyList<com.pyy.journalapp.timemachine.TimeCapsule>()) }
        var selectedEntry by remember { mutableStateOf<JournalEntry?>(null) }

        // 初始化示例数据
        LaunchedEffect(Unit) {
            journalEntries = listOf(
                JournalEntry(
                    id = "entry-1",
                    ownerId = "user-1",
                    bookId = "book-1",
                    title = "AI智能联想初体验",
                    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    visibility = Visibility.PUBLIC,
                    tags = listOf("AI", "体验", "智能"),
                    blocks = listOf(
                        TextBlock(
                            id = "text-1",
                            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                            orderIndex = 0,
                            text = "今天第一次使用AI智能联想到功能，感觉非常神奇！系统能够自动分析我的日记内容，并给出相关的标签和建议。",
                            format = TextFormat.PLAIN
                        )
                    )
                ),
                JournalEntry(
                    id = "entry-2",
                    ownerId = "user-1",
                    bookId = "book-1",
                    title = "时光胶囊：给未来的自己",
                    createdAt = Clock.System.now().minus(1.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                    updatedAt = Clock.System.now().minus(1.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                    visibility = Visibility.PRIVATE,
                    tags = listOf("时光胶囊", "未来", "期望"),
                    blocks = listOf(
                        TextBlock(
                            id = "text-2",
                            createdAt = Clock.System.now().minus(1.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                            updatedAt = Clock.System.now().minus(1.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                            orderIndex = 0,
                            text = "亲爱的未来的自己，现在的我对未来充满期待。希望一年后的你已经实现了现在的梦想。",
                            format = TextFormat.PLAIN
                        )
                    )
                ),
                JournalEntry(
                    id = "entry-3",
                    ownerId = "user-1",
                    bookId = "book-1",
                    title = "旅行日记：智能模板推荐",
                    createdAt = Clock.System.now().minus(2.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                    updatedAt = Clock.System.now().minus(2.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                    visibility = Visibility.PUBLIC,
                    tags = listOf("旅行", "模板", "推荐"),
                    blocks = listOf(
                        TextBlock(
                            id = "text-3",
                            createdAt = Clock.System.now().minus(2.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                            updatedAt = Clock.System.now().minus(2.days).toLocalDateTime(TimeZone.currentSystemDefault()),
                            orderIndex = 0,
                            text = "今天去了向往已久的城市，体验了当地的文化和美食。",
                            format = TextFormat.PLAIN
                        )
                    )
                )
            )

            // 创建一些示例时光胶囊
            val newCapsule = journalLifeCore.createTimeCapsule(
                journalEntries[1],
                Clock.System.now().plus(365.days).toLocalDateTime(TimeZone.currentSystemDefault()).date
            )
            timeCapsules = listOf(newCapsule)
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 顶部导航栏
            TopAppBar(
                title = { Text("JournalLifeApp", fontSize = 20.sp) },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { /* 主页菜单 */ }) {
                        Text("☰", fontSize = 20.sp) // 菜单图标替代
                    }
                },
                actions = {
                    IconButton(onClick = { /* 搜索 */ }) {
                        Text("🔍", fontSize = 20.sp) // 搜索图标替代
                    }
                    IconButton(onClick = { /* 设置 */ }) {
                        Text("⚙️", fontSize = 20.sp) // 设置图标替代
                    }
                }
            )

            // 主内容区域
            when (currentSection) {
                AppSection.Home -> HomePage(
                    journalLifeCore = journalLifeCore,
                    journalEntries = journalEntries,
                    timeCapsules = timeCapsules,
                    onNavigateToSection = { section -> currentSection = section },
                    onNavigateToEntry = { entry ->
                        selectedEntry = entry
                        currentSection = AppSection.EntryDetail
                    }
                )

                AppSection.AiInsights -> AiInsightsPage(
                    journalLifeCore = journalLifeCore,
                    journalEntries = journalEntries,
                    onBack = { currentSection = AppSection.Home }
                )

                AppSection.TimeCapsule -> TimeCapsulePage(
                    journalLifeCore = journalLifeCore,
                    timeCapsules = timeCapsules,
                    journalEntries = journalEntries,
                    onBack = { currentSection = AppSection.Home }
                )

                AppSection.ContextualWriting -> ContextualWritingPage(
                    journalLifeCore = journalLifeCore,
                    onBack = { currentSection = AppSection.Home },
                    onCreateEntry = { /* 创建新日记 */ }
                )

                AppSection.EntryDetail -> selectedEntry?.let { entry ->
                    EntryDetailPage(
                        entry = entry,
                        onBack = { currentSection = AppSection.Home }
                    )
                } ?: HomePage(
                    journalLifeCore = journalLifeCore,
                    journalEntries = journalEntries,
                    timeCapsules = timeCapsules,
                    onNavigateToSection = { section -> currentSection = section },
                    onNavigateToEntry = { entry ->
                        selectedEntry = entry
                        currentSection = AppSection.EntryDetail
                    }
                )
            }
        }
    }
}

/**
 * 应用程序主页面 - 展示核心功能概览
 */
@Composable
fun HomePage(
    journalLifeCore: JournalLifeCore,
    journalEntries: List<JournalEntry>,
    timeCapsules: List<com.pyy.journalapp.timemachine.TimeCapsule>,
    onNavigateToSection: (AppSection) -> Unit,
    onNavigateToEntry: (JournalEntry) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标语和简介
        item {
            Column(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "JournalLifeApp",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )
                Text(
                    text = "AI智能联想 · 时光胶囊 · 情境化创作",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // 功能入口卡片
        item {
            Text(
                text = "核心功能",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // AI智能联想卡片
        item {
            FeatureCard(
                title = "🧠 AI智能联想",
                description = "自动分析日记内容，提供智能标签和写作建议",
                icon = "⚡", // 使用字符串表示图标
                onClick = { onNavigateToSection(AppSection.AiInsights) },
                color = Color(0xFFFFB74D)
            )
        }

        // 时光胶囊卡片
        item {
            FeatureCard(
                title = "🎁 时光胶囊",
                description = "将记忆封存到未来，与未来的自己对话",
                icon = "⏳", // 使用字符串表示图标
                onClick = { onNavigateToSection(AppSection.TimeCapsule) },
                color = Color(0xFF81C784)
            )
        }

        // 情境化创作卡片
        item {
            FeatureCard(
                title = "✍️ 情境化创作",
                description = "基于当前情境，智能推荐写作模板",
                icon = "📝", // 使用字符串表示图标
                onClick = { onNavigateToSection(AppSection.ContextualWriting) },
                color = Color(0xFF4FC3F7)
            )
        }

        // 最近条目部分
        if (journalEntries.isNotEmpty()) {
            item {
                Text(
                    text = "最近记录",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            items(journalEntries.take(3)) { entry ->
                EntryCard(
                    entry = entry,
                    onClick = { onNavigateToEntry(entry) },
                    journalLifeCore = journalLifeCore
                )
            }
        }

        // 即将到期的时光胶囊
        val upcomingCapsules = remember {
            journalLifeCore.getUpcomingTimeCapsules(timeCapsules, 30)
        }

        if (upcomingCapsules.isNotEmpty()) {
            item {
                Text(
                    text = "即将开启的时光胶囊",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            items(upcomingCapsules) { capsule ->
                CapsuleCard(capsule = capsule)
            }
        }
    }
}

/**
 * AI智能联想到见解页面
 */
@Composable
fun AiInsightsPage(
    journalLifeCore: JournalLifeCore,
    journalEntries: List<JournalEntry>,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Text("←", fontSize = 20.sp) // 返回箭头替代图标
                }
                Text(
                    text = "AI智能联想",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Text(
                text = "AI智能联想到分析结果",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (journalEntries.isNotEmpty()) {
            items(journalEntries.take(3)) { entry ->
                val analysis = remember { journalLifeCore.analyzeJournalContent(entry) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = entry.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "关键词: ${analysis.keywords.take(5).joinToString(", ")}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "情绪: ${analysis.emotions.joinToString(", ")}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (analysis.suggestions.isNotEmpty()) {
                            Text(
                                text = "AI建议: ${analysis.suggestions.first()}",
                                fontSize = 14.sp,
                                color = Color(0xFF6200EE)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "AI写作建议",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "💡 提升写作质量的建议",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "1. 保持每日记录的习惯，即使是简短的想法\n" +
                               "2. 尝试不同的表达方式，丰富词汇多样性\n" +
                               "3. 定期回顾之前的条目，观察个人成长轨迹\n" +
                               "4. 利用时光胶囊功能，给未来的自己留言",
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

/**
 * 时光胶囊页面
 */
@Composable
fun TimeCapsulePage(
    journalLifeCore: JournalLifeCore,
    timeCapsules: List<com.pyy.journalapp.timemachine.TimeCapsule>,
    journalEntries: List<JournalEntry>,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Text("←", fontSize = 20.sp) // 返回箭头替代图标
                }
                Text(
                    text = "时光胶囊",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Text(
                text = "创建新的时光胶囊",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "选择要封存的日记",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (journalEntries.isNotEmpty()) {
                        val sampleEntry = journalEntries.first()
                        Text(
                            text = sampleEntry.title,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "设定开启时间",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val targetDate = remember {
                        Clock.System.now().plus(365.days).toLocalDateTime(TimeZone.currentSystemDefault()).date
                    }

                    Text(
                        text = "一年后 (${targetDate})",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* 处理创建胶囊 */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("创建时光胶囊")
                    }
                }
            }
        }

        item {
            Text(
                text = "我的时光胶囊",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        if (timeCapsules.isNotEmpty()) {
            items(timeCapsules) { capsule ->
                CapsuleCard(capsule = capsule)
            }
        } else {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = 2.dp
                ) {
                    Text(
                        text = "还没有创建时光胶囊，点击上方按钮开始创建第一个胶囊吧！",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            }
        }

        // 纪念日提醒
        item {
            Text(
                text = "今日纪念日",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val anniversaries = remember {
                journalLifeCore.getAnniversaryEntries(journalEntries, currentDate)
            }

            if (anniversaries.isNotEmpty()) {
                anniversaries.forEach { anniversary ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "📅 ${anniversary.yearsAgo}年前的今天",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = anniversary.originalEntry.title,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "今天没有历史上的记录",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * 情境化创作页面
 */
@Composable
fun ContextualWritingPage(
    journalLifeCore: JournalLifeCore,
    onBack: () -> Unit,
    onCreateEntry: () -> Unit
) {
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var selectedSeason by remember { mutableStateOf<Season?>(null) }
    var isTraveling by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Text("←", fontSize = 20.sp) // 返回箭头替代图标
                }
                Text(
                    text = "情境化创作",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Text(
                text = "选择当前情境",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Text("当前情绪:", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = MainAxisAlignment.Start,
                crossAxisAlignment = CrossAxisAlignment.Center,
                children = {
                    Mood.values().forEach { mood ->
                        ChoiceChip(
                            text = mood.name,
                            isSelected = selectedMood == mood,
                            onClick = { selectedMood = mood }
                        )
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("当前季节:", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = MainAxisAlignment.Start,
                crossAxisAlignment = CrossAxisAlignment.Center,
                children = {
                    Season.values().forEach { season ->
                        ChoiceChip(
                            text = season.name,
                            isSelected = selectedSeason == season,
                            onClick = { selectedSeason = season }
                        )
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isTraveling,
                    onCheckedChange = { isTraveling = it }
                )
                Text("正在旅行中")
            }
        }

        // 推荐模板
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "为您推荐的模板",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val context = remember(selectedMood, selectedSeason, isTraveling) {
                WritingContext(
                    mood = selectedMood,
                    season = selectedSeason,
                    isTraveling = isTraveling
                )
            }

            val recommendedTemplate = remember(context) {
                journalLifeCore.recommendTemplate(context)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = recommendedTemplate.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recommendedTemplate.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "建议包含的元素:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = recommendedTemplate.suggestedBlocks.joinToString(", ") {
                            when(it) {
                                is HeadingBlock -> "标题"
                                is TextBlock -> "文本"
                                is ImageBlock -> "图片"
                                is TodoBlock -> "待办"
                                is QuoteBlock -> "引用"
                                else -> "内容块"
                            }
                        },
                        fontSize = 14.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onCreateEntry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "使用模板开始写作",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * 条目详情页面
 */
@Composable
fun EntryDetailPage(entry: JournalEntry, onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Text("←", fontSize = 20.sp) // 返回箭头替代图标
                }
                Text(
                    text = "日记详情",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Text(
                text = entry.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "创建时间: ${entry.createdAt}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "可见性: ${if(entry.visibility == Visibility.PUBLIC) "公开" else "私密"}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            if (entry.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "标签: ${entry.tags.joinToString(", ")}",
                    fontSize = 14.sp,
                    color = Color(0xFF6200EE)
                )
            }
        }

        items(entry.blocks) { block ->
            Spacer(modifier = Modifier.height(12.dp))

            when (block) {
                is TextBlock -> {
                    Text(
                        text = block.text,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
                is HeadingBlock -> {
                    Text(
                        text = block.text,
                        fontSize = if (block.level == 1) 24.sp else if (block.level == 2) 20.sp else 18.sp,
                        fontWeight = if (block.level <= 2) FontWeight.Bold else FontWeight.Normal
                    )
                }
                is ImageBlock -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        elevation = 2.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🖼️ 图片占位符\n${block.imageId}",
                                fontSize = 14.sp
                            )
                        }
                    }
                    block.caption?.let { caption ->
                        Text(
                            text = caption,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.style.FontStyle.Italic,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                is TodoBlock -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = block.completed,
                            onCheckedChange = { /* 处理待办项状态变化 */ }
                        )
                        Text(
                            text = block.text,
                            fontSize = 16.sp,
                            textDecoration = if (block.completed) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                        )
                    }
                }
                is QuoteBlock -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 1.dp,
                        backgroundColor = Color(0xFFF5F5F5)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "\"${block.text}\"",
                                fontSize = 16.sp,
                                fontStyle = androidx.compose.ui.text.style.FontStyle.Italic
                            )
                            block.author?.let { author ->
                                Text(
                                    text = "- $author",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 功能卡片组件 - 支持图像向量图标
 */
@Composable
fun FeatureCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    color: Color = Color.LightGray
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * 功能卡片组件 - 支持文本图标（如emoji）
 */
@Composable
fun FeatureCard(
    title: String,
    description: String,
    icon: String,  // 文本图标，例如emoji
    onClick: () -> Unit,
    color: Color = Color.LightGray
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = icon,
                fontSize = 24.sp,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * 日记条目卡片组件
 */
@Composable
fun EntryCard(entry: JournalEntry, onClick: () -> Unit, journalLifeCore: JournalLifeCore) {
    val analysis = remember { journalLifeCore.analyzeJournalContent(entry) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = entry.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${entry.createdAt.date}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (analysis.keywords.isNotEmpty()) {
                Text(
                    text = "标签: ${analysis.keywords.take(3).joinToString(", ")}",
                    fontSize = 12.sp,
                    color = Color(0xFF6200EE)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            val sampleText = when(val firstBlock = entry.blocks.firstOrNull()) {
                is TextBlock -> firstBlock.text.take(80) + if (firstBlock.text.length > 80) "..." else ""
                else -> "暂无文本内容"
            }

            Text(
                text = sampleText,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2
            )
        }
    }
}

/**
 * 时光胶囊卡片组件
 */
@Composable
fun CapsuleCard(capsule: com.pyy.journalapp.timemachine.TimeCapsule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "🎁 ${capsule.originalEntry.title}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "目标日期: ${capsule.targetDate}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "创建日期: ${capsule.creationDate}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "状态: ${capsule.status}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * 选择芯片组件
 */
@Composable
fun ChoiceChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = if (isSelected) {
            ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
        } else {
            ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
        },
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

/**
 * 应用程序部分枚举
 */
enum class AppSection {
    Home, AiInsights, TimeCapsule, ContextualWriting, EntryDetail
}

/**
 * 流式布局组件
 */
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSize: SizeMode = SizeMode.Wrap,
    mainAxisAlignment: MainAxisAlignment = MainAxisAlignment.Start,
    crossAxisAlignment: CrossAxisAlignment = CrossAxisAlignment.Center,
    children: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        maxItemsInEachRow = Int.MAX_VALUE,
        itemSpacing = 8.dp,
        children = children
    )
}

/**
 * 尺寸模式枚举
 */
enum class SizeMode {
    Wrap, Expand
}