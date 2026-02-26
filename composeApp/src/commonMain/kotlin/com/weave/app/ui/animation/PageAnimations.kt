package com.weave.app.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

/**
 * 页面翻页动画
 *
 * @param isFlipping 是否正在翻页
 * @param direction 翻页方向（1 = 向前，-1 = 向后）
 * @param onFlipComplete 翻页完成回调
 * @param frontContent 正面内容
 * @param backContent 背面内容
 */
@Composable
fun PageFlipAnimation(
    isFlipping: Boolean,
    direction: Int = 1,
    onFlipComplete: () -> Unit = {},
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    val rotationY = remember { Animatable(0f) }
    val shadowAlpha = remember { Animatable(0f) }

    LaunchedEffect(isFlipping) {
        if (isFlipping) {
            // 翻页动画
            val targetRotation = 180f * direction

            launch {
                rotationY.animateTo(
                    targetValue = targetRotation,
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            launch {
                // 阴影效果：翻页过程中中间位置阴影最深
                shadowAlpha.animateTo(0.3f, tween(300))
                shadowAlpha.animateTo(0f, tween(300))
            }

            onFlipComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.rotationY = rotationY.value
                cameraDistance = 12f * density
            }
    ) {
        // 正面或背面内容
        if (rotationY.value < 90f) {
            frontContent()
        } else {
            Box(
                modifier = Modifier.graphicsLayer {
                    rotationY = 180f // 背面内容需要翻转回来
                }
            ) {
                backContent()
            }
        }

        // 阴影层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = shadowAlpha.value))
        )
    }
}

/**
 * 书本打开动画
 *
 * @param isOpening 是否正在打开
 * @param onOpenComplete 打开完成回调
 * @param content 内容
 */
@Composable
fun BookOpenAnimation(
    isOpening: Boolean,
    onOpenComplete: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(isOpening) {
        if (isOpening) {
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(400)
                )
            }
            onOpenComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            }
    ) {
        content()
    }
}

/**
 * 书本合上动画
 *
 * @param isClosing 是否正在合上
 * @param onCloseComplete 合上完成回调
 * @param content 内容
 */
@Composable
fun BookCloseAnimation(
    isClosing: Boolean,
    onCloseComplete: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val thickness = remember { Animatable(1f) }

    LaunchedEffect(isClosing) {
        if (isClosing) {
            launch {
                scale.animateTo(
                    targetValue = 0.1f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                thickness.animateTo(
                    targetValue = 20f,
                    animationSpec = tween(500)
                )
            }
            onCloseComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
    ) {
        content()
    }
}
