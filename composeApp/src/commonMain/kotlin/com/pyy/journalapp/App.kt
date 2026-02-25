package com.pyy.journalapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pyy.journalapp.models.*
import com.pyy.journalapp.screens.*
import com.pyy.journalapp.ui.theme.JournalAppTheme
import com.pyy.journalapp.utils.IdGenerator
import com.pyy.journalapp.viewmodels.*
import com.pyy.journalapp.ai.AISuggestionEngine
import com.pyy.journalapp.timemachine.TimeCapsuleManager
import com.pyy.journalapp.templates.TemplateManager
import com.pyy.journalapp.core.JournalLifeCore
import com.pyy.journalapp.utils.DateTimeUtils

@Composable
fun App() {
    JournalAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            // 使用内存中的数据（简化版，不使用真实数据库避免驱动问题）
            // 在后续迭代中可以使用 expect/actual 模式创建平台特定的驱动

            val smartJournalViewModel = remember {
                SmartJournalViewModel(
                    aiSuggestionEngine = AISuggestionEngine(),
                    timeCapsuleManager = TimeCapsuleManager(),
                    templateManager = TemplateManager()
                )
            }

            val journalLifeCore = remember { JournalLifeCore() }

            // 使用状态管理书册列表
            var books by remember {
                mutableStateOf(
                    listOf(
                        Book(
                            id = IdGenerator.generateId(),
                            ownerId = "user-1",
                            title = "我的日记",
                            description = "记录生活的点点滴滴",
                            visibilityDefault = Visibility.PRIVATE,
                            createdAt = DateTimeUtils.now(),
                            updatedAt = DateTimeUtils.now()
                        ),
                        Book(
                            id = IdGenerator.generateId(),
                            ownerId = "user-1",
                            title = "旅行笔记",
                            description = "记录旅途中的美好时光",
                            visibilityDefault = Visibility.PUBLIC,
                            createdAt = DateTimeUtils.now(),
                            updatedAt = DateTimeUtils.now()
                        ),
                        Book(
                            id = IdGenerator.generateId(),
                            ownerId = "user-1",
                            title = "读书笔记",
                            description = "阅读思考与心得",
                            visibilityDefault = Visibility.PRIVATE,
                            createdAt = DateTimeUtils.now(),
                            updatedAt = DateTimeUtils.now()
                        )
                    )
                )
            }

            // 选中的书籍状态
            var selectedBook by remember { mutableStateOf<Book?>(null) }
            var selectedJournal by remember { mutableStateOf<JournalEntry?>(null) }

            // 模拟日记条目
            val sampleJournalEntries = remember {
                mutableStateListOf(
                    JournalEntry(
                        id = IdGenerator.generateId(),
                        ownerId = "user-1",
                        bookId = "book-1",
                        title = "AI智能分析示例",
                        createdAt = DateTimeUtils.now(),
                        updatedAt = DateTimeUtils.now(),
                        visibility = Visibility.PUBLIC,
                        tags = listOf("AI", "分析", "建议"),
                        blocks = emptyList(),
                        metricsCache = MetricsCache(wordCount = 45, imageCount = 0)
                    ),
                    JournalEntry(
                        id = IdGenerator.generateId(),
                        ownerId = "user-1",
                        bookId = "book-1",
                        title = "时光胶囊：致未来的自己",
                        createdAt = DateTimeUtils.now(),
                        updatedAt = DateTimeUtils.now(),
                        visibility = Visibility.PRIVATE,
                        tags = listOf("时光胶囊", "未来", "期望"),
                        blocks = emptyList(),
                        metricsCache = MetricsCache(wordCount = 32, imageCount = 0)
                    )
                )
            }

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        books = books,
                        onBookClick = { book ->
                            selectedBook = book
                            navController.navigate("bookdetail")
                        },
                        onAddBookClick = {
                            navController.navigate("addbook")
                        },
                        onSettingsClick = {
                            navController.navigate("settings")
                        }
                    )
                }

                composable("addbook") {
                    AddBookScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onCreateBook = { title, description, visibility ->
                            // 添加新书册
                            val newBook = Book(
                                id = IdGenerator.generateId(),
                                ownerId = "user-1",
                                title = title,
                                description = description.takeIf { it.isNotBlank() },
                                visibilityDefault = visibility,
                                createdAt = DateTimeUtils.now(),
                                updatedAt = DateTimeUtils.now()
                            )
                            books = books + newBook
                            navController.popBackStack()
                        }
                    )
                }

                composable("bookdetail") {
                    val book = selectedBook
                    if (book != null) {
                        val entries = sampleJournalEntries.filter { it.bookId == book.id }

                        BookDetailScreen(
                            bookTitle = book.title,
                            journalEntries = entries,
                            onJournalClick = { journal ->
                                selectedJournal = journal
                                navController.navigate("journalview")
                            },
                            onAddJournalClick = {
                                navController.navigate("editor")
                            },
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onExportImagesClick = {
                                println("导出书籍图片功能触发")
                            }
                        )
                    }
                }

                composable("journalview") {
                    val journal = selectedJournal
                    if (journal != null) {
                        JournalViewScreen(
                            journal = journal,
                            onBackClick = { navController.popBackStack() },
                            onEditClick = {
                                navController.navigate("editor")
                            },
                            onExportClick = { },
                            onPrivacyToggle = { },
                            onExportImagesClick = { }
                        )
                    }
                }

                composable("editor") {
                    val recommendedTemplate = remember {
                        smartJournalViewModel.recommendTemplate()
                    }

                    val currentBook = selectedBook

                    JournalEditorScreen(
                        journalTitle = "",
                        blocks = recommendedTemplate.generateDefaultContent(),
                        onTitleChange = { },
                        onBlockChange = { index, block -> },
                        onBlockAdd = { },
                        onBlockDelete = { index -> },
                        onSaveClick = {
                            if (currentBook != null) {
                                val newEntry = JournalEntry(
                                    id = IdGenerator.generateId(),
                                    ownerId = "user-1",
                                    bookId = currentBook.id,
                                    title = "新日记",
                                    createdAt = DateTimeUtils.now(),
                                    updatedAt = DateTimeUtils.now(),
                                    blocks = recommendedTemplate.generateDefaultContent()
                                )
                                sampleJournalEntries.add(newEntry)
                            }
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
                    TimeCapsuleCreator(
                        onTimeCapsuleCreated = { targetDate ->
                            println("创建时光胶囊，目标日期: $targetDate")
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }

                composable("statistics") {
                    // 计算实际统计数据
                    val stats = remember(sampleJournalEntries.size) {
                        StatisticsData(
                            totalEntries = sampleJournalEntries.size,
                            totalWords = sampleJournalEntries.sumOf { it.metricsCache.wordCount },
                            totalImages = sampleJournalEntries.sumOf { it.metricsCache.imageCount },
                            writingFrequency = emptyMap(),
                            wordsPerWeek = emptyMap(),
                            topTags = sampleJournalEntries
                                .flatMap { it.tags }
                                .groupingBy { it }
                                .eachCount()
                        )
                    }
                    StatisticsScreen(
                        stats = stats,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("settings") {
                    SettingsScreen(
                        currentTheme = Theme.SYSTEM,
                        currentPrivacyDefault = Visibility.PRIVATE,
                        onThemeChange = { },
                        onPrivacyDefaultChange = { },
                        onLogoutClick = { },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
