package com.pyy.journalapp.components.bookshelf

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.Book

/**
 * 书架组件 - 展示书本的容器
 *
 * @param books 书册列表
 * @param selectedBook 当前选中的书册（用于动画）
 * @param onBookClick 点击书本回调
 * @param modifier 修饰符
 */
@Composable
fun Bookshelf(
    books: List<Book>,
    selectedBook: Book?,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 书架标题
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📚",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "我的书架",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Text(
                    text = "${books.size} 本",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 书架层板
            if (books.isEmpty()) {
                EmptyBookshelf()
            } else {
                ShelfRow(
                    books = books,
                    selectedBook = selectedBook,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

/**
 * 书架层板 - 包含书本和木质纹理
 */
@Composable
private fun ShelfRow(
    books: List<Book>,
    selectedBook: Book?,
    onBookClick: (Book) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 书架背景/层板
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            // 上层书架空间
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .drawBehind {
                        // 书架背板
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFF5E6D3),
                                    Color(0xFFE8D5C4)
                                )
                            )
                        )

                        // 添加一些木纹效果
                        repeat(10) { i ->
                            drawLine(
                                color = Color(0xFFD4C4B0).copy(alpha = 0.3f),
                                start = Offset(0f, i * size.height / 10),
                                end = Offset(size.width, i * size.height / 10),
                                strokeWidth = 1f
                            )
                        }
                    }
            ) {
                // 书本排列
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    items(books, key = { it.id }) { book ->
                        val isSelected = selectedBook?.id == book.id

                        // 使用简单的条件显示，避免 LazyRow 中的 AnimatedVisibility 作用域问题
                        if (!isSelected) {
                            BookOnShelf(
                                book = book,
                                onClick = { onBookClick(book) }
                            )
                        } else {
                            // 占位保持布局稳定
                            Spacer(modifier = Modifier.width(85.dp))
                        }
                    }
                }
            }

            // 书架层板边框
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .drawBehind {
                        // 层板顶部（亮色）
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFD4A574),
                                    Color(0xFFC4956A)
                                )
                            ),
                            size = androidx.compose.ui.geometry.Size(
                                size.width,
                                size.height * 0.6f
                            )
                        )

                        // 层板底部（阴影）
                        drawRect(
                            color = Color(0xFF8B7355),
                            topLeft = Offset(0f, size.height * 0.6f),
                            size = androidx.compose.ui.geometry.Size(
                                size.width,
                                size.height * 0.4f
                            )
                        )

                        // 高光效果
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = Offset(0f, 1f),
                            end = Offset(size.width, 1f),
                            strokeWidth = 1f
                        )
                    }
            )
        }
    }
}

/**
 * 空书架状态
 */
@Composable
private fun EmptyBookshelf() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📚",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "书架空空如也",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "点击 + 按钮创建你的第一本日记",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 书本展开动画容器
 * 用于从书架到详情页的过渡
 */
@Composable
fun BookExpandableContainer(
    book: Book?,
    isExpanded: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isExpanded && book != null,
        enter = expandIn(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ) + fadeIn(tween(300)),
        exit = shrinkOut(
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) + fadeOut(tween(200))
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            // 背景遮罩
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onClose() }
            )

            // 内容区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                content()
            }
        }
    }
}
