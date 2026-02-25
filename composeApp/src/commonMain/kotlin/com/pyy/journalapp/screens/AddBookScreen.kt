package com.pyy.journalapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.Visibility

/**
 * 添加书册界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onBackClick: () -> Unit,
    onCreateBook: (title: String, description: String, visibility: Visibility) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(Visibility.PRIVATE) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "新建书册",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("✕", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 书册图标预览
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                BookPreview(
                    title = title.takeIf { it.isNotBlank() } ?: "新书册",
                    visibility = visibility
                )
            }

            // 书册名称输入
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    showError = false
                },
                label = { Text("书册名称 *") },
                placeholder = { Text("给你的书册起个名字") },
                isError = showError && title.isBlank(),
                supportingText = if (showError && title.isBlank()) {
                    { Text("书册名称不能为空") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // 书册描述输入
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("书册描述") },
                placeholder = { Text("简单描述一下这本书册的用途（可选）") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp)
            )

            // 可见性选择
            Text(
                text = "可见性设置",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            VisibilitySelector(
                selected = visibility,
                onSelect = { visibility = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            // 创建按钮
            Button(
                onClick = {
                    if (title.isBlank()) {
                        showError = true
                    } else {
                        onCreateBook(title, description, visibility)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank()
            ) {
                Text(
                    text = "创建书册",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * 书册预览
 */
@Composable
private fun BookPreview(
    title: String,
    visibility: Visibility,
    modifier: Modifier = Modifier
) {
    val gradientColors = when (visibility) {
        Visibility.PUBLIC -> listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer
        )
        Visibility.PRIVATE -> listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    }

    Surface(
        modifier = modifier
            .width(120.dp)
            .height(160.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                )
        ) {
            // 内容
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 图标
                Text(
                    text = if (visibility == Visibility.PUBLIC) "🌐" else "🔒",
                    style = MaterialTheme.typography.titleMedium
                )

                // 书名
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 可见性选择器
 */
@Composable
private fun VisibilitySelector(
    selected: Visibility,
    onSelect: (Visibility) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VisibilityOption(
            icon = "🔒",
            title = "私密",
            description = "只有你自己可以查看",
            selected = selected == Visibility.PRIVATE,
            onClick = { onSelect(Visibility.PRIVATE) }
        )

        VisibilityOption(
            icon = "🌐",
            title = "公开",
            description = "所有人都可以查看",
            selected = selected == Visibility.PUBLIC,
            onClick = { onSelect(Visibility.PUBLIC) }
        )
    }
}

/**
 * 可见性选项
 */
@Composable
private fun VisibilityOption(
    icon: String,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        border = if (selected) {
            BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, style = MaterialTheme.typography.titleMedium)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (selected) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
