package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.*
import com.pyy.journalapp.ui.theme.JournalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalViewScreen(
    journal: JournalEntry,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onExportClick: () -> Unit,
    onPrivacyToggle: () -> Unit,
    onExportImagesClick: () -> Unit = {}, // 新增导出图片功能
    modifier: Modifier = Modifier
) {
    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(journal.title) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Text("<-") // Fallback back icon
                        }
                    },
                    actions = {
                        // More options menu
                        var expanded by remember { mutableStateOf(false) }
                        IconButton(onClick = { expanded = true }) {
                            Text("⋮") // Fallback more options icon
                        }
                        
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("编辑") },
                                onClick = {
                                    expanded = false
                                    onEditClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("导出") },
                                onClick = {
                                    expanded = false
                                    onExportClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("导出图片") }, // 新增导出图片选项
                                onClick = {
                                    expanded = false
                                    onExportImagesClick() // 调用新的导出图片功能
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (journal.visibility == Visibility.PUBLIC) "设为私密" else "设为公开"
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    onPrivacyToggle()
                                }
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    // Journal metadata
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Created: ${formatDate(journal.createdAt)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Updated: ${formatDate(journal.updatedAt)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = journal.visibility.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (journal.visibility == Visibility.PUBLIC) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                itemsIndexed(journal.blocks) { _, block ->
                    BlockViewer(
                        block = block,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun BlockViewer(
    block: Block,
    modifier: Modifier = Modifier
) {
    when (block) {
        is TextBlock -> {
            Card(
                modifier = modifier,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = block.text,
                    style = when (block.style.bold) {
                        true -> MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        else -> MaterialTheme.typography.bodyMedium
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }
        is ImageBlock -> {
            Card(
                modifier = modifier,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    // In a real app, this would show the actual image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🖼️ 图片: ${block.imageId}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    val captionText = block.caption
                    if (captionText != null && captionText.isNotEmpty()) {
                        Text(
                            text = captionText,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        is TodoBlock -> {
            Card(
                modifier = modifier,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Checkbox(
                        checked = block.completed,
                        onCheckedChange = null // Read-only
                    )
                    Text(
                        text = block.text,
                        style = if (block.completed) {
                            MaterialTheme.typography.bodyMedium.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                        } else {
                            MaterialTheme.typography.bodyMedium
                        }
                    )
                }
            }
        }
        is DividerBlock -> {
            Divider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
        is QuoteBlock -> {
            Card(
                modifier = modifier,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "💬 \"${block.text}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
        is HeadingBlock -> {
            Text(
                text = when (block.level) {
                    1 -> "📑 ${block.text}"
                    2 -> "🏷️ ${block.text}"
                    3 -> "🔖 ${block.text}"
                    else -> "📝 ${block.text}"
                },
                style = when (block.level) {
                    1 -> MaterialTheme.typography.headlineLarge
                    2 -> MaterialTheme.typography.headlineMedium
                    3 -> MaterialTheme.typography.headlineSmall
                    else -> MaterialTheme.typography.titleLarge
                },
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp)
            )
        }
    }
}

// Helper function to format date
private fun formatDate(dateTime: kotlinx.datetime.LocalDateTime): String {
    return "${dateTime.monthNumber}/${dateTime.dayOfMonth}/${dateTime.year}"
}