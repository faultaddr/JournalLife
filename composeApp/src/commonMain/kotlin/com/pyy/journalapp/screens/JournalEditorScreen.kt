package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.*
import com.pyy.journalapp.ui.theme.JournalAppTheme
import com.pyy.journalapp.viewmodels.SmartJournalViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun JournalEditorScreen(
    journalTitle: String,
    blocks: List<Block>,
    onTitleChange: (String) -> Unit,
    onBlockChange: (Int, Block) -> Unit,
    onBlockAdd: (Block) -> Unit,
    onBlockDelete: (Int) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    smartJournalViewModel: SmartJournalViewModel = SmartJournalViewModel()  // 注入智能日记视图模型
) {
    var title by remember { mutableStateOf(journalTitle) }

    LaunchedEffect(journalTitle) {
        title = journalTitle
    }

    // 使用智能建议
    val aiSuggestions by smartJournalViewModel.aiSuggestions.collectAsState()

    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("编辑日记") },
                    navigationIcon = {
                        IconButton(onClick = { /* Navigate back */ }) {
                            Text("<-") // 返回图标作为文本
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            // 保存前执行AI分析
                            val currentEntry = JournalEntry(
                                id = Uuid.random().toString(),
                                ownerId = Uuid.random().toString(),
                                bookId = "temp_book_id",
                                title = title,
                                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                                blocks = blocks
                            )
                            smartJournalViewModel.analyzeContent(currentEntry)
                            onSaveClick()
                        }) {
                            Text("✅") // 保存图标作为文本
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        // Add a new text block by default
                        val newTextBlock = TextBlock(
                            id = Uuid.random().toString(),
                            createdAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                            updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                            orderIndex = blocks.size,
                            text = ""
                        )
                        onBlockAdd(newTextBlock)
                    },
                    icon = { Text("➕") }, // 更好的添加图标
                    text = { Text("添加内容") }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        onTitleChange(it)
                    },
                    label = { Text("日记标题") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // AI建议面板
                AISuggestionPanel(
                    contentAnalysis = aiSuggestions,
                    onSuggestionClick = { suggestion ->
                        // 当用户点击建议时，可以添加到内容中
                        println("用户选择了AI建议: $suggestion")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Blocks list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    itemsIndexed(blocks) { index, block ->
                        BlockEditor(
                            block = block,
                            onBlockChange = { updatedBlock -> onBlockChange(index, updatedBlock) },
                            onDeleteClick = { onBlockDelete(index) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BlockEditor(
    block: Block,
    onBlockChange: (Block) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Block header with type and delete button
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (block) {
                        is TextBlock -> "📝 文本块"
                        is ImageBlock -> "🖼️ 图片块"
                        is TodoBlock -> "✅ 待办块"
                        is DividerBlock -> "— 分隔线"
                        is QuoteBlock -> "💬 引用块"
                        is HeadingBlock -> "📑 标题块"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = onDeleteClick) {
                    Text("🗑️") // 更好的删除图标
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (block) {
                is TextBlock -> {
                    OutlinedTextField(
                        value = block.text,
                        onValueChange = { newText ->
                            val updatedBlock = block.copy(
                                text = newText,
                                updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            )
                            onBlockChange(updatedBlock)
                        },
                        label = { Text("输入文字...") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is ImageBlock -> {
                    // For now, just show a placeholder for image block
                    OutlinedTextField(
                        value = block.imageId,
                        onValueChange = { newImageId ->
                            val updatedBlock = block.copy(
                                imageId = newImageId,
                                updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            )
                            onBlockChange(updatedBlock)
                        },
                        label = { Text("图片ID或路径") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // In a real app, this would show an image picker and preview
                }
                is TodoBlock -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = block.completed,
                            onCheckedChange = { completed ->
                                val updatedBlock = block.copy(
                                    completed = completed,
                                    updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                )
                                onBlockChange(updatedBlock)
                            }
                        )
                        OutlinedTextField(
                            value = block.text,
                            onValueChange = { newText ->
                                val updatedBlock = block.copy(
                                    text = newText,
                                    updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                )
                                onBlockChange(updatedBlock)
                            },
                            label = { Text("待办事项...") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                is DividerBlock -> {
                    Divider(modifier = Modifier.fillMaxWidth())
                    Text("分隔线", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is QuoteBlock -> {
                    OutlinedTextField(
                        value = block.text,
                        onValueChange = { newText ->
                            val updatedBlock = block.copy(
                                text = newText,
                                updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            )
                            onBlockChange(updatedBlock)
                        },
                        label = { Text("引用内容...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is HeadingBlock -> {
                    OutlinedTextField(
                        value = block.text,
                        onValueChange = { newText ->
                            val updatedBlock = block.copy(
                                text = newText,
                                updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            )
                            onBlockChange(updatedBlock)
                        },
                        label = { Text("标题文字...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Allow selecting heading level
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("级别: ")
                        Spacer(modifier = Modifier.width(8.dp))
                        // Simple level selector
                        repeat(6) { level ->
                            val isSelected = block.level == level + 1
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    val updatedBlock = block.copy(
                                        level = level + 1,
                                        updatedAt = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                    )
                                    onBlockChange(updatedBlock)
                                },
                                label = { Text("${level + 1}") }
                            )
                        }
                    }
                }
            }
        }
    }
}