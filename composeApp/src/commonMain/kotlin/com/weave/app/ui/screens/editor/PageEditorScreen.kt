package com.weave.app.ui.screens.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.hapticfeedback.LocalHapticFeedback
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.rememberAsyncImagePainter
import com.weave.app.data.model.Page
import com.weave.app.data.model.PageElement
import com.weave.app.data.model.PaperStyle
import com.weave.app.data.model.PhotoElement
import com.weave.app.data.model.StickerElement
import com.weave.app.data.model.TapeElement
import com.weave.app.data.model.TextElement
import com.weave.app.ui.components.physics.PhysicsSticker
import com.weave.app.ui.theme.WeaveColors
import kotlinx.coroutines.delay

/**
 * 页面编辑器 - 核心创作区域
 *
 * @param page 当前页面
 * @param elements 页面元素列表
 * @param onElementAdd 添加元素回调
 * @param onElementUpdate 更新元素回调
 * @param onElementRemove 删除元素回调
 * @param onBackClick 返回回调
 * @param onSaveClick 保存回调
 */
@Composable
fun PageEditorScreen(
    page: Page?,
    elements: List<PageElement>,
    onElementAdd: (PageElement) -> Unit = {},
    onElementUpdate: (PageElement) -> Unit = {},
    onElementRemove: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WeaveColors.WoodMedium)
    ) {
        // 纸张背景
        page?.let {
            PaperBackground(
                paperStyle = it.paperStyle,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
        }

        // 元素渲染层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            elements.forEach { element ->
                when (element) {
                    is StickerElement -> RenderStickerElement(
                        element = element,
                        onUpdate = onElementUpdate,
                        onRemove = { onElementRemove(element.id) }
                    )
                    is TapeElement -> RenderTapeElement(
                        element = element,
                        onUpdate = onElementUpdate
                    )
                    is PhotoElement -> RenderPhotoElement(
                        element = element,
                        onUpdate = onElementUpdate,
                        onRemove = { onElementRemove(element.id) }
                    )
                    is TextElement -> RenderTextElement(
                        element = element,
                        onUpdate = onElementUpdate
                    )
                }
            }
        }

        // 顶部工具栏
        EditorToolbar(
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

/**
 * 纸张背景 - 带纹理效果
 */
@Composable
private fun PaperBackground(
    paperStyle: PaperStyle,
    modifier: Modifier = Modifier
) {
    val backgroundColor = WeaveColors.getPaperColor(paperStyle)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(4.dp),
                clip = false
            )
            .background(backgroundColor, RoundedCornerShape(4.dp))
    ) {
        // 根据纸张样式绘制纹理
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (paperStyle) {
                PaperStyle.RULED -> {
                    // 绘制横线
                    val lineSpacing = 40f
                    for (i in 1..(size.height / lineSpacing).toInt()) {
                        drawLine(
                            color = Color(0xFFCCCCCC),
                            start = Offset(0f, i * lineSpacing),
                            end = Offset(size.width, i * lineSpacing),
                            strokeWidth = 1f
                        )
                    }
                }
                PaperStyle.GRID -> {
                    // 绘制网格
                    val gridSpacing = 40f
                    for (i in 0..(size.width / gridSpacing).toInt()) {
                        drawLine(
                            color = Color(0xFFCCCCCC),
                            start = Offset(i * gridSpacing, 0f),
                            end = Offset(i * gridSpacing, size.height),
                            strokeWidth = 1f
                        )
                    }
                    for (i in 0..(size.height / gridSpacing).toInt()) {
                        drawLine(
                            color = Color(0xFFCCCCCC),
                            start = Offset(0f, i * gridSpacing),
                            end = Offset(size.width, i * gridSpacing),
                            strokeWidth = 1f
                        )
                    }
                }
                PaperStyle.DOT -> {
                    // 绘制点阵
                    val dotSpacing = 40f
                    for (i in 0..(size.width / dotSpacing).toInt()) {
                        for (j in 0..(size.height / dotSpacing).toInt()) {
                            drawCircle(
                                color = Color(0xFFCCCCCC),
                                radius = 2f,
                                center = Offset(i * dotSpacing, j * dotSpacing)
                            )
                        }
                    }
                }
                else -> { /* 空白或特殊纹理纸张 */ }
            }
        }
    }
}

/**
 * 渲染贴纸元素
 */
@Composable
private fun RenderStickerElement(
    element: StickerElement,
    onUpdate: (PageElement) -> Unit,
    onRemove: () -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .offset(element.positionX.dp, element.positionY.dp)
            .rotate(element.rotation)
            .scale(element.scale * if (isDragging) 1.05f else 1f)
            .zIndex(element.zIndex.toFloat())
            .shadow(4.dp, RoundedCornerShape(4.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        // 放置震动
                    }
                ) { change, dragAmount ->
                    change.consume()
                    onUpdate(
                        element.copy(
                            positionX = element.positionX + dragAmount.x,
                            positionY = element.positionY + dragAmount.y
                        )
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(element.stickerId),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * 渲染胶带元素
 */
@Composable
private fun RenderTapeElement(
    element: TapeElement,
    onUpdate: (PageElement) -> Unit
) {
    Canvas(
        modifier = Modifier
            .offset(element.positionX.dp, element.positionY.dp)
            .rotate(element.rotation)
            .zIndex(element.zIndex.toFloat())
            .size(element.width.dp, element.height.dp)
    ) {
        // 绘制胶带主体
        drawRect(
            color = Color(0x80FFD700), // 半透明金色
            size = Size(element.width, element.height)
        )

        // 绘制撕裂边缘
        element.ripPattern.forEach { point ->
            drawCircle(
                color = Color(0xFFFFD700),
                radius = 3f,
                center = point.toOffset()
            )
        }
    }
}

/**
 * 渲染照片元素 - 带拍立得效果和显影动画
 */
@Composable
private fun RenderPhotoElement(
    element: PhotoElement,
    onUpdate: (PageElement) -> Unit,
    onRemove: () -> Unit
) {
    var developProgress by remember { mutableStateOf(element.developProgress) }

    // 显影动画
    LaunchedEffect(element.id) {
        if (developProgress < 1f) {
            while (developProgress < 1f) {
                delay(50)
                developProgress += 0.02f
                onUpdate(element.copy(developProgress = developProgress))
            }
        }
    }

    val haptic = LocalHapticFeedback.current
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .offset(element.positionX.dp, element.positionY.dp)
            .rotate(element.rotation)
            .scale(element.scale * if (isDragging) 1.05f else 1f)
            .zIndex(element.zIndex.toFloat())
            .shadow(6.dp, RoundedCornerShape(4.dp))
            .background(WeaveColors.PaperWhite, RoundedCornerShape(4.dp))
            .padding(8.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    onUpdate(
                        element.copy(
                            positionX = element.positionX + dragAmount.x,
                            positionY = element.positionY + dragAmount.y
                        )
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 照片图像
            Image(
                painter = rememberAsyncImagePainter(element.photoUri),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .alpha(developProgress)
                    .background(Color.Black),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(
                    ColorMatrix().apply {
                        setToSaturation(developProgress)
                    }
                )
            )
            // 拍立得白边
            if (element.polaroidStyle) {
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.Text(
                    text = element.caption.takeIf { it.isNotBlank() } ?: "",
                    fontSize = 12.sp,
                    color = WeaveColors.InkBlack,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}

/**
 * 渲染文字元素
 */
@Composable
private fun RenderTextElement(
    element: TextElement,
    onUpdate: (PageElement) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .offset(element.positionX.dp, element.positionY.dp)
            .rotate(element.rotation)
            .scale(element.scale)
            .zIndex(element.zIndex.toFloat())
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    onUpdate(
                        element.copy(
                            positionX = element.positionX + dragAmount.x,
                            positionY = element.positionY + dragAmount.y
                        )
                    )
                }
            }
    ) {
        androidx.compose.material3.Text(
            text = element.content,
            fontSize = element.fontSize.sp,
            color = element.getInkColor()
        )
    }
}

/**
 * 编辑器工具栏
 */
@Composable
private fun EditorToolbar(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "返回",
                tint = WeaveColors.PaperWhite
            )
        }

        IconButton(
            onClick = onSaveClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "保存",
                tint = WeaveColors.AccentGold
            )
        }
    }
}
