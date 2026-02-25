package com.pyy.journalapp.components.bookshelf

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyy.journalapp.models.Book
import com.pyy.journalapp.models.Visibility
import kotlinx.datetime.LocalDateTime

/**
 * 3D书本封面组件
 *
 * @param book 书册数据
 * @param isOpen 是否处于打开状态
 * @param onClick 点击回调
 * @param modifier 修饰符
 * @param coverColor 封面颜色
 */
@Composable
fun BookCover3D(
    book: Book,
    isOpen: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    coverColor: Color = MaterialTheme.colorScheme.primary
) {
    val gradientColors = remember(book.id) {
        generateBookColors(book.id)
    }

    // 打开状态的动画
    val openProgress by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "open_progress"
    )

    // 悬浮效果
    val elevation by animateFloatAsState(
        targetValue = if (isOpen) 20f else 4f,
        animationSpec = tween(300),
        label = "elevation"
    )

    Box(
        modifier = modifier
            .width(100.dp)
            .height(140.dp)
            .graphicsLayer {
                // 3D透视效果
                val rotationY = -15f + (openProgress * 25f)
                this.rotationY = rotationY
                this.cameraDistance = 12f * density
                this.shadowElevation = elevation
            }
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // 书本主体
        BookShape(
            book = book,
            gradientColors = gradientColors,
            openProgress = openProgress
        )
    }
}

/**
 * 书本形状绘制
 */
@Composable
private fun BookShape(
    book: Book,
    gradientColors: List<Color>,
    openProgress: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val width = size.width
                val height = size.height
                val spineWidth = width * 0.12f
                val cornerRadius = 8.dp.toPx()

                // 绘制书脊（左侧）
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            gradientColors[0].darken(0.3f),
                            gradientColors[0].darken(0.1f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(spineWidth, 0f)
                    ),
                    size = Size(spineWidth, height)
                )

                // 绘制封面主体
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(spineWidth, 0f),
                        end = Offset(width, height)
                    ),
                    topLeft = Offset(spineWidth, 0f),
                    size = Size(width - spineWidth, height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )

                // 绘制封面边框/装饰线
                drawRect(
                    color = Color.White.copy(alpha = 0.3f),
                    topLeft = Offset(spineWidth + 8f, 12f),
                    size = Size(width - spineWidth - 16f, 2f)
                )

                // 书脊装订线效果
                repeat(3) { i ->
                    val lineX = spineWidth * (0.25f + i * 0.25f)
                    drawLine(
                        color = Color.Black.copy(alpha = 0.1f),
                        start = Offset(lineX, 0f),
                        end = Offset(lineX, height),
                        strokeWidth = 1f
                    )
                }
            }
            .padding(start = 12.dp, top = 20.dp, end = 8.dp, bottom = 12.dp)
    ) {
        // 书本内容
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 书名（竖排效果通过限制宽度和换行实现）
            Text(
                text = book.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(70.dp)
            )

            // 底部信息
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = if (book.visibilityDefault == Visibility.PUBLIC) "🌐" else "🔒",
                    fontSize = 10.sp
                )
                Text(
                    text = "${book.entriesCount}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

/**
 * 书架上的书本（收起状态）
 */
@Composable
fun BookOnShelf(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientColors = remember(book.id) {
        generateBookColors(book.id)
    }

    // 悬浮动画
    var isHovered by remember { mutableStateOf(false) }
    val hoverOffset by animateFloatAsState(
        targetValue = if (isHovered) -8f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "hover"
    )

    Box(
        modifier = modifier
            .width(85.dp)
            .height(120.dp)
            .offset(y = hoverOffset.dp)
            .clickable {
                isHovered = true
                onClick()
            }
            .drawBehind {
                val width = size.width
                val height = size.height
                val spineWidth = width * 0.15f
                val cornerRadius = 4.dp.toPx()

                // 底部阴影
                drawRect(
                    color = Color.Black.copy(alpha = 0.2f),
                    topLeft = Offset(0f, height - 4f),
                    size = Size(width, 4f)
                )

                // 书脊
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            gradientColors[0].darken(0.4f),
                            gradientColors[0].darken(0.2f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(spineWidth, 0f)
                    ),
                    size = Size(spineWidth, height)
                )

                // 封面
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientColors[0],
                            gradientColors[1]
                        )
                    ),
                    topLeft = Offset(spineWidth, 0f),
                    size = Size(width - spineWidth, height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        cornerRadius,
                        cornerRadius
                    )
                )

                // 书脊文字效果（简化版）
                drawRect(
                    color = Color.White.copy(alpha = 0.15f),
                    topLeft = Offset(2f, 20f),
                    size = Size(spineWidth - 4f, height - 40f)
                )
            }
            .padding(start = 16.dp, top = 12.dp, end = 6.dp, bottom = 8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        // 封面文字（横向）
        Text(
            text = book.title.take(6) + if (book.title.length > 6) "…" else "",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 11.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(60.dp)
        )
    }
}

/**
 * 生成书本颜色
 */
private fun generateBookColors(bookId: String): List<Color> {
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
private fun Color.darken(factor: Float): Color {
    return Color(
        red = (red * (1 - factor)).coerceIn(0f, 1f),
        green = (green * (1 - factor)).coerceIn(0f, 1f),
        blue = (blue * (1 - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

private val Int.absoluteValue: Int
    get() = if (this < 0) -this else this

// 扩展属性，用于获取书册的日记数量
private val Book.entriesCount: Int
    get() = 3 // 模拟数据，实际应从数据库获取
