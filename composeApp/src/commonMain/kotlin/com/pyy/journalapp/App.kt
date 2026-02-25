package com.pyy.journalapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pyy.journalapp.models.*
import com.pyy.journalapp.screens.*
import com.pyy.journalapp.ui.theme.JournalAppTheme
import com.pyy.journalapp.utils.ExportManager
import com.pyy.journalapp.utils.IdGenerator
import com.pyy.journalapp.viewmodels.ExportViewModel
import com.pyy.journalapp.viewmodels.SmartJournalViewModel
import com.pyy.journalapp.ai.AISuggestionEngine
import com.pyy.journalapp.timemachine.TimeCapsuleManager
import com.pyy.journalapp.templates.TemplateManager
import com.pyy.journalapp.core.JournalLifeCore
import com.pyy.journalapp.utils.DateTimeUtils
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

@Composable
fun App() {
    JournalAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            // 初始化JournalLifeApp的核心功能整合类
            val journalLifeCore = remember {
                JournalLifeCore()
            }

            // 创建智能日记视图模型
            val smartJournalViewModel = remember {
                SmartJournalViewModel(
                    aiSuggestionEngine = AISuggestionEngine(),
                    timeCapsuleManager = TimeCapsuleManager(),
                    templateManager = TemplateManager()
                )
            }

            // 创建一些假数据
            val sampleBooks = remember {
                listOf(
                    Book(
                        id = IdGenerator.generateId(),
                        ownerId = IdGenerator.generateId(),
                        title = "AI智能联想日记",
                        coverImageId = null,
                        description = "通过AI分析内容并提供智能建议",
                        visibilityDefault = Visibility.PUBLIC,
                        createdAt = LocalDateTime(2024, Month.JANUARY, 15, 10, 30),
                        updatedAt = LocalDateTime(2024, Month.DECEMBER, 1, 15, 45)
                    ),
                    Book(
                        id = IdGenerator.generateId(),
                        ownerId = IdGenerator.generateId(),
                        title = "时光胶囊收藏",
                        coverImageId = null,
                        description = "封存记忆，传递给未来的自己",
                        visibilityDefault = Visibility.PRIVATE,
                        createdAt = LocalDateTime(2024, Month.FEBRUARY, 10, 9, 15),
                        updatedAt = LocalDateTime(2024, Month.NOVEMBER, 20, 14, 30)
                    ),
                    Book(
                        id = IdGenerator.generateId(),
                        ownerId = IdGenerator.generateId(),
                        title = "情境化创作集",
                        coverImageId = null,
                        description = "基于情境的智能模板推荐",
                        visibilityDefault = Visibility.PUBLIC,
                        createdAt = LocalDateTime(2024, Month.MARCH, 5, 12, 0),
                        updatedAt = LocalDateTime(2024, Month.DECEMBER, 10, 18, 20)
                    )
                )
            }

            val sampleJournalEntries = remember {
                listOf(
                    JournalEntry(
                        id = IdGenerator.generateId(),
                        ownerId = sampleBooks[0].ownerId,
                        bookId = sampleBooks[0].id,
                        title = "AI智能分析示例",
                        createdAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                        updatedAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                        visibility = Visibility.PUBLIC,
                        tags = listOf("AI", "分析", "建议"),
                        blocks = listOf(
                            HeadingBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                                updatedAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                                orderIndex = 0,
                                text = "AI智能联想体验",
                                level = 1
                            ),
                            TextBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                                updatedAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                                orderIndex = 1,
                                text = "今天体验了AI智能联想功能，系统能够自动分析我的日记内容并提供相关的标签和建议。",
                                style = TextStyle(),
                                format = TextFormat.PLAIN
                            ),
                            QuoteBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                                updatedAt = LocalDateTime(2024, Month.APRIL, 10, 11, 20),
                                orderIndex = 2,
                                text = "技术让写作变得更智能、更有趣。",
                                author = "用户"
                            )
                        ),
                        metricsCache = MetricsCache(
                            wordCount = 45,
                            imageCount = 0
                        )
                    ),
                    JournalEntry(
                        id = IdGenerator.generateId(),
                        ownerId = sampleBooks[1].ownerId,
                        bookId = sampleBooks[1].id,
                        title = "时光胶囊：致未来的自己",
                        createdAt = LocalDateTime(2024, Month.MAY, 5, 14, 30),
                        updatedAt = LocalDateTime(2024, Month.MAY, 5, 14, 30),
                        visibility = Visibility.PRIVATE,
                        tags = listOf("时光胶囊", "未来", "期望"),
                        blocks = listOf(
                            HeadingBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.MAY, 5, 14, 30),
                                updatedAt = LocalDateTime(2024, Month.MAY, 5, 14, 30),
                                orderIndex = 0,
                                text = "给未来自己的信",
                                level = 1
                            ),
                            TextBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.MAY, 5, 14, 30),
                                updatedAt = LocalDateTime(2024, Month.MAY, 5, 14, 30),
                                orderIndex = 1,
                                text = "亲爱的未来的自己，现在的我充满期待，希望一年后的你能比现在更成熟、更有智慧。",
                                style = TextStyle(),
                                format = TextFormat.PLAIN
                            )
                        ),
                        metricsCache = MetricsCache(
                            wordCount = 32,
                            imageCount = 0
                        )
                    ),
                    JournalEntry(
                        id = IdGenerator.generateId(),
                        ownerId = sampleBooks[2].ownerId,
                        bookId = sampleBooks[2].id,
                        title = "情境化创作：旅行模板",
                        createdAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                        updatedAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                        visibility = Visibility.PUBLIC,
                        tags = listOf("旅行", "模板", "推荐"),
                        blocks = listOf(
                            HeadingBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                                updatedAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                                orderIndex = 0,
                                text = "今日行程",
                                level = 1
                            ),
                            TextBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                                updatedAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                                orderIndex = 1,
                                text = "今天的景点是：北京故宫博物院",
                                style = TextStyle(),
                                format = TextFormat.PLAIN
                            ),
                            TextBlock(
                                id = IdGenerator.generateId(),
                                createdAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                                updatedAt = LocalDateTime(2024, Month.MAY, 10, 10, 15),
                                orderIndex = 2,
                                text = "今日感受：宏伟壮观，历史文化底蕴深厚。",
                                style = TextStyle(),
                                format = TextFormat.PLAIN
                            )
                        ),
                        metricsCache = MetricsCache(
                            wordCount = 28,
                            imageCount = 0
                        )
                    )
                )
            }

            val sampleStats = remember {
                StatisticsData(
                    totalEntries = 42,
                    totalWords = 8450,
                    totalImages = 36,
                    writingFrequency = mapOf(
                        "2024-01" to 8,
                        "2024-02" to 12,
                        "2024-03" to 6,
                        "2024-04" to 10,
                        "2024-05" to 6
                    ),
                    wordsPerWeek = mapOf(
                        "Week 1" to 1200,
                        "Week 2" to 850,
                        "Week 3" to 1100,
                        "Week 4" to 950
                    ),
                    topTags = mapOf(
                        "AI智能联想" to 15,
                        "时光胶囊" to 12,
                        "情境化创作" to 8,
                        "旅行" to 6,
                        "生活" to 10
                    )
                )
            }

            val exportManager = ExportManager()

            // 选中的书籍状态
            var selectedBook by remember { mutableStateOf<Book?>(null) }

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        books = sampleBooks,
                        onBookClick = { book ->
                            selectedBook = book  // 保存选中的书籍
                            navController.navigate("bookdetail")
                        },
                        onAddBookClick = {
                            // 智能建议：分析用户的书写模式并推荐新主题
                            println("AI建议：您可以尝试写一本关于「技能提升」的手账")
                        }
                    )
                }
                composable("bookdetail") {
                    val book = selectedBook ?: sampleBooks[0]  // 使用选中的书籍或默认第一本书
                    val isExporting = false
                    val exportResult = null
                    val progress = 0

                    androidx.compose.foundation.layout.Box {
                        BookDetailScreen(
                            bookTitle = book.title,
                            journalEntries = sampleJournalEntries.filter { it.bookId == book.id },
                            onJournalClick = { journal ->
                                navController.navigate("journalview")
                            },
                            onAddJournalClick = {
                                navController.navigate("editor")
                            },
                            onBackClick = {  // 添加返回回调
                                navController.popBackStack()
                            },
                            onExportImagesClick = {
                                // 使用AI分析当前书籍内容并提供建议
                                smartJournalViewModel.analyzeContent(sampleJournalEntries.first())
                                println("导出书籍图片功能触发 - 模拟导出过程")
                            }
                        )

                        // 显示导出进度对话框
                        if (false) { // 简化，暂时不显示进度
                            ExportProgressDialog(
                                isVisible = isExporting,
                                progress = progress,
                                message = exportResult ?: "正在导出图片...",
                                onCancel = { /* 不需要处理取消 */ }
                            )
                        }
                    }
                }
                composable("journalview") {
                    val isExporting = false
                    val exportResult = null
                    val progress = 0

                    androidx.compose.foundation.layout.Box {
                        JournalViewScreen(
                            journal = sampleJournalEntries[0],
                            onBackClick = { navController.popBackStack() },
                            onEditClick = {
                                navController.navigate("editor")
                            },
                            onExportClick = { /* Handle export click */ },
                            onPrivacyToggle = { /* Handle privacy toggle */ },
                            onExportImagesClick = {
                                // 使用AI分析当前日记内容并提供建议
                                smartJournalViewModel.analyzeContent(sampleJournalEntries[0])
                                println("导出日记图片功能触发 - 模拟导出过程")
                            }
                        )

                        // 显示导出进度对话框
                        if (false) { // 简化，暂时不显示进度
                            ExportProgressDialog(
                                isVisible = isExporting,
                                progress = progress,
                                message = exportResult ?: "正在导出图片...",
                                onCancel = { /* 不需要处理取消 */ }
                            )
                        }
                    }
                }
                composable("editor") {
                    // 当打开编辑器时，根据上下文推荐模板
                    val recommendedTemplate = remember {
                        smartJournalViewModel.recommendTemplate()
                    }

                    JournalEditorScreen(
                        journalTitle = "新日记",
                        blocks = recommendedTemplate.generateDefaultContent(),
                        onTitleChange = { /* Handle title change */ },
                        onBlockChange = { index, block -> /* Handle block change */ },
                        onBlockAdd = { /* Handle block addition */ },
                        onBlockDelete = { index -> /* Handle block deletion */ },
                        onSaveClick = {
                            // 保存时执行AI分析
                            val newEntry = JournalEntry(
                                id = IdGenerator.generateId(),
                                ownerId = IdGenerator.generateId(),
                                bookId = sampleBooks[0].id,
                                title = "新日记",
                                createdAt = DateTimeUtils.now(),
                                updatedAt = DateTimeUtils.now(),
                                blocks = recommendedTemplate.generateDefaultContent()
                            )

                            smartJournalViewModel.analyzeContent(newEntry)

                            // Navigate back
                            navController.popBackStack()
                        }
                    )
                }
                composable("aiinsights") {
                    AIInsightsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable("timecapsule") {
                    // 时光胶囊界面，整合AI智能联想和情境化创作
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "🔮 时光胶囊 & AI助手",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // 展示如何将三大功能结合
                        Text(
                            text = "JournalLifeApp 结合了三大核心功能：",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "1. AI智能联想：分析您的日记内容，提供智能标签和建议\n" +
                                   "2. 时光胶囊：将今天的记忆封存，发送给未来的自己\n" +
                                   "3. 情境化创作：根据当前情境推荐最适合的写作模板",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                        )

                        TimeCapsuleCreator(
                            onTimeCapsuleCreated = { targetDate ->
                                // 在实际应用中，这里会创建时光胶囊
                                println("创建时光胶囊，目标日期: $targetDate")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )

                        // 展示AI分析结果
                        sampleJournalEntries.forEach { entry ->
                            val analysis by remember { mutableStateOf(journalLifeCore.analyzeJournalContent(entry)) }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = "AI分析: ${entry.title}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "关键词: ${analysis.keywords.take(3).joinToString(", ")}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "情绪: ${analysis.emotions.joinToString(", ")}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "建议: ${analysis.suggestions.firstOrNull() ?: "无"}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
                composable("statistics") {
                    StatisticsScreen(
                        stats = sampleStats,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable("settings") {
                    SettingsScreen(
                        currentTheme = Theme.SYSTEM,
                        currentPrivacyDefault = Visibility.PRIVATE,
                        onThemeChange = { /* Handle theme change */ },
                        onPrivacyDefaultChange = { /* Handle privacy change */ },
                        onLogoutClick = { /* Handle logout */ },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}