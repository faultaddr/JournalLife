package com.weave.app.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.delay
import kotlin.math.ln
import kotlin.random.Random

/**
 * 胶带撕裂效果
 *
 * @param startPoint 起始点
 * @param endPoint 结束点
 * @param color 胶带颜色
 * @param width 胶带宽度
 * @param onRipComplete 撕裂完成回调
 */
@Composable
fun TapeRippingEffect(
    startPoint: Offset,
    endPoint: Offset,
    color: Color = Color(0xFFFFD700),
    width: Float = 20f,
    onRipComplete: (List<Offset>) -> Unit = {}
) {
    val progress = remember { Animatable(0f) }
    val ripPoints = remember { mutableStateListOf<Offset>() }

    LaunchedEffect(Unit) {
        // 生成撕裂路径
        val totalSteps = 20
        val stepProgress = 1f / totalSteps

        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearEasing
            )
        ) {
            // 在动画过程中生成不规则撕裂点
            val currentStep = (value / stepProgress).toInt()
            while (ripPoints.size < currentStep && ripPoints.size < totalSteps) {
                val t = ripPoints.size * stepProgress
                val basePoint = lerp(startPoint, endPoint, t)
                // 添加随机偏移模拟撕裂不规则性
                val randomOffset = Offset(
                    x = Random.nextFloat() * 8f - 4f,
                    y = Random.nextFloat() * 4f - 2f
                )
                ripPoints.add(basePoint + randomOffset)
            }
        }

        // 确保终点被添加
        if (ripPoints.isEmpty() || ripPoints.last() != endPoint) {
            ripPoints.add(endPoint)
        }

        onRipComplete(ripPoints.toList())
    }

    // 绘制撕裂胶带
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (ripPoints.size >= 2) {
            // 绘制胶带主体
            val path = Path().apply {
                moveTo(startPoint.x, startPoint.y - width / 2)

                // 上边缘
                ripPoints.forEach { point ->
                    lineTo(point.x, point.y - width / 2)
                }

                // 右端
                lineTo(endPoint.x, endPoint.y + width / 2)

                // 下边缘（逆向）
                ripPoints.reversed().forEach { point ->
                    lineTo(point.x, point.y + width / 2)
                }

                close()
            }

            drawPath(
                path = path,
                color = color.copy(alpha = 0.8f)
            )

            // 绘制撕裂边缘细节
            drawPath(
                path = Path().apply {
                    moveTo(startPoint.x, startPoint.y - width / 2)
                    ripPoints.forEach { point ->
                        lineTo(point.x, point.y - width / 2)
                    }
                },
                color = color,
                style = Stroke(width = 1f, cap = StrokeCap.Round)
            )

            drawPath(
                path = Path().apply {
                    moveTo(startPoint.x, startPoint.y + width / 2)
                    ripPoints.forEach { point ->
                        lineTo(point.x, point.y + width / 2)
                    }
                },
                color = color,
                style = Stroke(width = 1f, cap = StrokeCap.Round)
            )
        }
    }
}

/**
 * 线性插值
 */
private fun lerp(start: Offset, end: Offset, fraction: Float): Offset {
    return Offset(
        x = start.x + (end.x - start.x) * fraction,
        y = start.y + (end.y - start.y) * fraction
    )
}

/**
 * 拍立得显影动画
 *
 * @param isDeveloping 是否正在显影
 * @param durationMs 显影持续时间
 * @param onDevelopComplete 显影完成回调
 * @param content 内容
 */
@Composable
fun PolaroidDevelopAnimation(
    isDeveloping: Boolean,
    durationMs: Int = 2000,
    onDevelopComplete: () -> Unit = {},
    content: @Composable (progress: Float) -> Unit
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isDeveloping) {
        if (isDeveloping) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = durationMs,
                    easing = LinearEasing
                )
            )
            onDevelopComplete()
        }
    }

    content(progress.value)
}

/**
 * 启动仪式动画
 *
 * @param isActive 是否激活
 * @param onRitualComplete 仪式完成回调
 * @param content 内容
 */
@Composable
fun OpeningRitualAnimation(
    isActive: Boolean,
    onRitualComplete: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val darknessAlpha = remember { Animatable(0f) }

    LaunchedEffect(isActive) {
        if (isActive) {
            // 屏幕变暗
            darknessAlpha.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(300)
            )

            // 停顿一下
            delay(500)

            // 恢复正常
            darknessAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(500)
            )

            onRitualComplete()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        content()

        // 暗色遮罩
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Black.copy(alpha = darknessAlpha.value)
            )
        }
    }
}
