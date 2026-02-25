package com.pyy.journalapp.components.bookshelf

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyy.journalapp.models.Book
import kotlinx.datetime.LocalDateTime

/**
 * 书本打开动画组件
 * 模拟真实书本打开的3D效果
 *
 * @param book 书册数据
 * @param isOpen 是否打开
 * @param onClose 关闭回调
 * @param content 书页内容
 */
@Composable
fun BookOpenAnimation(
    book: Book?,
    isOpen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = { DefaultBookPages(book) }
) {
    if (book == null) return

    val gradientColors = remember(book.id) {
        generateBookColorsForOpen(book.id)
    }

    // 打开进度动画
    val openProgress by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "open_progress"
    )

    // 书本整体缩放动画（从书架到中央）
    val scale by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    // 透明度动画
    val alpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(400),
        label = "alpha"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 背景遮罩
        if (openProgress > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f * openProgress))
                    .clickable(enabled = isOpen) { onClose() }
            )
        }

        // 书本容器
        if (openProgress > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f)
                    .graphicsLayer {
                        this.scaleX = scale
                        this.scaleY = scale
                        this.alpha = alpha
                    },
                contentAlignment = Alignment.Center
            ) {
                // 书本3D结构
                Book3DStructure(
                    book = book,
                    gradientColors = gradientColors,
                    openProgress = openProgress,
                    onClose = onClose,
                    content = content
                )
            }
        }
    }
}

/**
 * 书本3D结构 - 包含封面、书页和翻页效果
 */
@Composable
private fun Book3DStructure(
    book: Book,
    gradientColors: List<Color>,
    openProgress: Float,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 书本阴影
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .graphicsLayer {
                    translationY = 20f * (1 - openProgress)
                    shadowElevation = 30f * openProgress
                }
                .background(
                    color = Color.Black.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // 书本主体
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // 左页（封面背面或内容）
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .graphicsLayer {
                        // 左页保持不动或轻微旋转
                        rotationY = -5f * openProgress
                        cameraDistance = 20f * density
                    }
                    .drawBehind {
                        val width = size.width
                        val height = size.height

                        // 页面背景
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFDF6E3),
                                    Color(0xFFF5E6D3)
                                ),
                                start = Offset(width, 0f),
                                end = Offset(0f, height)
                            )
                        )

                        // 左侧阴影（书脊处）
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent
                                ),
                                startX = 0f,
                                endX = 20f
                            ),
                            size = androidx.compose.ui.geometry.Size(20f, height)
                        )

                        // 页面纹理线条
                        repeat(20) { i ->
                            drawLine(
                                color = Color(0xFFE8D5C4).copy(alpha = 0.3f),
                                start = Offset(0f, i * height / 20),
                                end = Offset(width, i * height / 20),
                                strokeWidth = 0.5f
                            )
                        }
                    }
                    .padding(16.dp)
            ) {
                // 左页内容
                LeftPageContent(book = book, gradientColors = gradientColors)
            }

            // 书脊（中间装订处）
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .fillMaxHeight()
                    .graphicsLayer {
                        cameraDistance = 20f * density
                    }
                    .drawBehind {
                        val width = size.width
                        val height = size.height

                        // 书脊渐变
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    gradientColors[0].darkenForOpen(0.4f),
                                    gradientColors[0].darkenForOpen(0.2f),
                                    gradientColors[0],
                                    gradientColors[0].darkenForOpen(0.2f),
                                    gradientColors[0].darkenForOpen(0.4f)
                                )
                            )
                        )

                        // 装订线效果
                        repeat(5) { i ->
                            val x = width * (0.2f + i * 0.15f)
                            drawLine(
                                color = Color.Black.copy(alpha = 0.15f),
                                start = Offset(x, 0f),
                                end = Offset(x, height),
                                strokeWidth = 1.5f
                            )
                        }
                    }
            )

            // 右页（主要内容页）
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .graphicsLayer {
                        // 右页翻页效果
                        rotationY = 15f * (1 - openProgress)
                        cameraDistance = 20f * density
                    }
                    .drawBehind {
                        val width = size.width
                        val height = size.height

                        // 页面背景
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFDF6E3),
                                    Color(0xFFF5E6D3)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(width, height)
                            )
                        )

                        // 右侧阴影（书脊处）
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.2f)
                                ),
                                startX = width - 20f,
                                endX = width
                            ),
                            topLeft = Offset(width - 20f, 0f),
                            size = androidx.compose.ui.geometry.Size(20f, height)
                        )

                        // 页面纹理线条
                        repeat(20) { i ->
                            drawLine(
                                color = Color(0xFFE8D5C4).copy(alpha = 0.3f),
                                start = Offset(0f, i * height / 20),
                                end = Offset(width, i * height / 20),
                                strokeWidth = 0.5f
                            )
                        }
                    }
                    .padding(16.dp)
            ) {
                // 右页内容
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 关闭按钮
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onClose
                        ) {
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // 页面内容
                    content()
                }
            }
        }

        // 翻页动画覆盖层（封面翻开的动画）
        if (openProgress < 0.9f) {
            CoverFlipAnimation(
                book = book,
                gradientColors = gradientColors,
                flipProgress = 1f - openProgress
            )
        }
    }
}

/**
 * 封面翻转动画
 */
@Composable
private fun CoverFlipAnimation(
    book: Book,
    gradientColors: List<Color>,
    flipProgress: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = flipProgress,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "flip"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
            .graphicsLayer {
                // 封面从右向左翻转
                val rotation = -180f * animatedProgress
                rotationY = rotation
                cameraDistance = 25f * density

                // 翻转时的透视效果
                if (animatedProgress > 0.5f) {
                    alpha = (1f - animatedProgress) * 2f
                }
            }
            .drawBehind {
                val width = size.width * 0.5f
                val height = size.height

                // 封面
                drawRect(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(width, height)
                    ),
                    size = androidx.compose.ui.geometry.Size(width, height)
                )

                // 封面边框装饰
                drawRect(
                    color = Color.White.copy(alpha = 0.3f),
                    topLeft = Offset(16f, 24f),
                    size = androidx.compose.ui.geometry.Size(width - 32f, 3f)
                )

                // 书名区域
                drawRect(
                    color = Color.White.copy(alpha = 0.1f),
                    topLeft = Offset(24f, height * 0.3f),
                    size = androidx.compose.ui.geometry.Size(width - 48f, height * 0.4f)
                )
            }
    ) {
        // 封面内容
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(120.dp)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 左页内容
 */
@Composable
private fun LeftPageContent(
    book: Book,
    gradientColors: List<Color>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 书册图标/装饰
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            gradientColors[0].copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(40.dp)
                )
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = "📖",
                fontSize = 40.sp
            )
        }

        // 书册信息
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        book.description?.let { desc ->
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 统计信息
        BookStats(book = book)
    }
}

/**
 * 书册统计信息
 */
@Composable
private fun BookStats(book: Book) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatRow(icon = "✎", label = "日记", value = "${book.entriesCount} 篇")
        StatRow(
            icon = if (book.visibilityDefault == com.pyy.journalapp.models.Visibility.PUBLIC) "🌐" else "🔒",
            label = "可见性",
            value = if (book.visibilityDefault == com.pyy.journalapp.models.Visibility.PUBLIC) "公开" else "私密"
        )
        StatRow(icon = "📅", label = "创建", value = formatBookDate(book.createdAt))
    }
}

/**
 * 统计行
 */
@Composable
private fun StatRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 14.sp)
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}

/**
 * 默认书页内容
 */
@Composable
private fun DefaultBookPages(book: Book?) {
    if (book == null) return

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "最新日记",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        // 模拟日记条目
        repeat(3) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "日记条目 #${index + 1}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "这是日记的预览内容...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * 生成书本颜色
 */
private fun generateBookColorsForOpen(bookId: String): List<Color> {
    val palettes = listOf(
        listOf(Color(0xFF8B4513), Color(0xFFA0522D)), // 皮革棕
        listOf(Color(0xFF2F4F4F), Color(0xFF708090)), // 深石板灰
        listOf(Color(0xFF800080), Color(0xFF9932CC)), // 紫色
        listOf(Color(0xFF006400), Color(0xFF228B22)), // 森林绿
        listOf(Color(0xFF8B0000), Color(0xFFB22222)), // 深红
        listOf(Color(0xFF191970), Color(0xFF4169E1)), // 午夜蓝
        listOf(Color(0xFF556B2F), Color(0xFF6B8E23)), // 橄榄绿
        listOf(Color(0xFF483D8B), Color(0xFF6A5ACD)), // 石板蓝
    )

    val index = bookId.hashCode().absoluteValue % palettes.size
    return palettes[index]
}

/**
 * 颜色变暗辅助函数
 */
private fun Color.darkenForOpen(factor: Float): Color {
    return Color(
        red = (red * (1 - factor)).coerceIn(0f, 1f),
        green = (green * (1 - factor)).coerceIn(0f, 1f),
        blue = (blue * (1 - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

/**
 * 格式化日期
 */
private fun formatBookDate(dateTime: LocalDateTime): String {
    return "${dateTime.year}/${dateTime.monthNumber}/${dateTime.dayOfMonth}"
}

private val Int.absoluteValue: Int
    get() = if (this < 0) -this else this

// 扩展属性
private val Book.entriesCount: Int
    get() = 3 // 模拟数据
