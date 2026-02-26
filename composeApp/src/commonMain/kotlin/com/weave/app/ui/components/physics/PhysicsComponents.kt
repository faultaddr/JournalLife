package com.weave.app.ui.components.physics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.hapticfeedback.LocalHapticFeedback
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.rememberAsyncImagePainter
import com.weave.app.core.physics.PhysicsEngine
import com.weave.app.data.model.StickerElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * 物理贴纸组件 - 带掉落和拖拽效果
 *
 * @param element 贴纸元素数据
 * @param isNew 是否为新添加的元素（播放掉落动画）
 * @param onPositionChange 位置变化回调
 * @param onDragStart 拖拽开始回调
 * @param onDragEnd 拖拽结束回调
 * @param onZIndexChange Z-Index 变化回调
 */
@Composable
fun PhysicsSticker(
    element: StickerElement,
    isNew: Boolean = false,
    onPositionChange: (Offset) -> Unit = {},
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    onZIndexChange: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val physicsEngine = remember { PhysicsEngine() }

    // 动画状态
    val offsetX = remember { Animatable(element.positionX) }
    val offsetY = remember { Animatable(element.positionY) }
    val rotation = remember { Animatable(element.rotation) }
    val scale = remember { Animatable(element.scale) }

    // 新元素掉落动画
    LaunchedEffect(isNew) {
        if (isNew) {
            val startY = element.positionY - 300f

            // 播放掉落动画
            launch {
                offsetY.animateTo(
                    targetValue = element.positionY,
                    animationSpec = keyframes {
                        durationMillis = 800
                        element.positionY * 0.7f at 400 with LinearOutSlowInEasing
                        element.positionY * 0.9f at 600 with LinearOutSlowInEasing
                        element.positionY at 800 with FastOutSlowInEasing
                    }
                )
            }

            launch {
                val targetRotation = element.rotation + Random.nextFloat() * 30f - 15f
                rotation.animateTo(
                    targetValue = targetRotation,
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
                )
            }

            launch {
                delay(700)
                scale.animateTo(0.95f, tween(80))
                scale.animateTo(1.05f, tween(80))
                scale.animateTo(1f, tween(100))
                // 着陆震动
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    // 更新位置当元素数据变化时
    LaunchedEffect(element.positionX, element.positionY) {
        if (!offsetX.isRunning) {
            offsetX.snapTo(element.positionX)
        }
        if (!offsetY.isRunning) {
            offsetY.snapTo(element.positionY)
        }
    }

    Box(
        modifier = Modifier
            .offset(offsetX.value.dp, offsetY.value.dp)
            .rotate(rotation.value)
            .scale(scale.value)
            .zIndex(element.zIndex.toFloat())
            .shadow(
                elevation = physicsEngine.calculateShadowElevation(
                    offsetY.value,
                    element.positionY
                ),
                shape = RoundedCornerShape(4.dp),
                clip = false
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        // 开始拖拽震动
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onDragStart()
                        // 提升层级
                        onZIndexChange()
                        // 放大效果
                        launch {
                            scale.animateTo(1.05f, tween(150))
                        }
                    },
                    onDragEnd = {
                        onDragEnd()
                        // 缩小回原尺寸
                        launch {
                            scale.animateTo(1f, tween(150))
                        }
                        // 放置震动
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    launch {
                        offsetX.snapTo(offsetX.value + dragAmount.x)
                        offsetY.snapTo(offsetY.value + dragAmount.y)
                        // 根据拖拽速度计算轻微旋转
                        val velocityRotation = (dragAmount.x * 0.3f).coerceIn(-5f, 5f)
                        rotation.snapTo(element.rotation + velocityRotation)
                    }
                    onPositionChange(Offset(offsetX.value, offsetY.value))
                }
            }
    ) {
        // 贴纸图片
        Image(
            painter = rememberAsyncImagePainter(
                model = element.stickerId, // 这里应该是资源路径
                contentScale = ContentScale.Fit
            ),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * 可拖拽元素容器
 *
 * @param modifier 修饰符
 * @param initialPosition 初始位置
 * @param onPositionChange 位置变化回调
 * @param content 内容
 */
@Composable
fun DraggableElement(
    modifier: Modifier = Modifier,
    initialPosition: Offset = Offset.Zero,
    onPositionChange: (Offset) -> Unit = {},
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val offsetX = remember { Animatable(initialPosition.x) }
    val offsetY = remember { Animatable(initialPosition.y) }

    Box(
        modifier = modifier
            .offset(offsetX.value.dp, offsetY.value.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    onDragEnd = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    launch {
                        offsetX.snapTo(offsetX.value + dragAmount.x)
                        offsetY.snapTo(offsetY.value + dragAmount.y)
                    }
                    onPositionChange(Offset(offsetX.value, offsetY.value))
                }
            }
    ) {
        content()
    }
}
