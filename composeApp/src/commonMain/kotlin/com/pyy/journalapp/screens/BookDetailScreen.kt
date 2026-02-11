package com.pyy.journalapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.ui.theme.JournalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookTitle: String,
    journalEntries: List<JournalEntry>,
    onJournalClick: (JournalEntry) -> Unit,
    onAddJournalClick: () -> Unit,
    onExportImagesClick: () -> Unit = {}, // 新增导出图片功能
    modifier: Modifier = Modifier
) {
    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(bookTitle) },
                    navigationIcon = {
                        IconButton(onClick = { /* Navigate back */ }) {
                            Text("<-") // 返回图标作为文本
                        }
                    },
                    actions = {
                        // 导出图片按钮
                        IconButton(onClick = onExportImagesClick) {
                            Text("📤") // 导出图片图标
                        }

                        IconButton(onClick = onAddJournalClick) {
                            Text("➕") // 更好的加号图标
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onAddJournalClick,
                    icon = { Text("📝") }, // 笔记图标
                    text = { Text("新建日记") }
                )
            }
        ) { paddingValues ->
            if (journalEntries.isEmpty()) {
                Box(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "还没有日记条目\n点击右上角+按钮创建第一个",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(journalEntries) { journal ->
                        JournalEntryItem(
                            journal = journal,
                            onClick = { onJournalClick(journal) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalEntryItem(
    journal: JournalEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = journal.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${journal.visibility.name.lowercase()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (journal.visibility == com.pyy.journalapp.models.Visibility.PUBLIC)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "更新: ${formatDate(journal.updatedAt)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Helper function to format date
private fun formatDate(dateTime: kotlinx.datetime.LocalDateTime): String {
    return "${dateTime.monthNumber}/${dateTime.dayOfMonth}/${dateTime.year}"
}