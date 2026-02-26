package com.weave.app.core.physics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * 物理状态
 */
data class PhysicsState(
    val position: Offset,
    val rotation: Float,
    val scale: Float,
    val velocity: Offset = Offset.Zero,
    val isLanded: Boolean = false
)

/**
 * 物理引擎 - 处理手账元素的物理效果
 */
class PhysicsEngine {
    companion object {
        // 重力加速度 (dp/s^2)
        const val GRAVITY = 2000f

        // 弹性系数
        const val BOUNCE_DAMPING = 0.6f

        // 最大旋转角度
        const val MAX_ROTATION_OFFSET = 15f

        // 着陆时的压缩比例
        const val LANDING_SQUASH = 0.95f
    }

    /**
     * 模拟元素掉落动画
     *
     * @param startPosition 起始位置
     * @param targetPosition 目标位置
     * @param initialRotation 初始旋转角度
     * @param onUpdate 状态更新回调
     * @param onLand 着陆回调
     */
    suspend fun simulateDrop(
        startPosition: Offset,
        targetPosition: Offset,
        initialRotation: Float = 0f,
        onUpdate: (PhysicsState) -> Unit,
        onLand: () -> Unit = {}
    ) = coroutineScope {
        val positionX = Animatable(startPosition.x)
        val positionY = Animatable(startPosition.y)
        val rotation = Animatable(initialRotation)
        val scale = Animatable(1f)

        // 计算随机旋转偏移
        val targetRotation = initialRotation + Random.nextFloat() * MAX_ROTATION_OFFSET * 2 - MAX_ROTATION_OFFSET

        // 水平位置动画（轻微摆动）
        val horizontalOffset = Random.nextFloat() * 20f - 10f

        // 并行执行动画
        launch {
            positionX.animateTo(
                targetValue = targetPosition.x + horizontalOffset,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            // 重力下落 - 使用 keyframes 模拟抛物线
            positionY.animateTo(
                targetValue = targetPosition.y,
                animationSpec = keyframes {
                    durationMillis = 800
                    // 快速下落
                    targetPosition.y * 0.7f at 400 with LinearOutSlowInEasing
                    // 触底前减速
                    targetPosition.y * 0.9f at 600 with LinearOutSlowInEasing
                    // 触底 - 带弹跳
                    targetPosition.y at 800 with FastOutSlowInEasing
                }
            )
        }

        launch {
            // 旋转动画
            rotation.animateTo(
                targetValue = targetRotation,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            // 着陆时的压缩效果
            delay(700)
            scale.animateTo(LANDING_SQUASH, tween(80))
            scale.animateTo(1.05f, tween(80))
            scale.animateTo(1f, tween(100))
        }

        // 实时更新状态
        var lastTime = System.currentTimeMillis()
        while (!positionY.isRunning && !rotation.isRunning && !scale.isRunning) {
            delay(16) // ~60fps
            onUpdate(
                PhysicsState(
                    position = Offset(positionX.value, positionY.value),
                    rotation = rotation.value,
                    scale = scale.value,
                    isLanded = !positionY.isRunning
                )
            )
        }

        // 最终状态
        onUpdate(
            PhysicsState(
                position = targetPosition,
                rotation = targetRotation,
                scale = 1f,
                isLanded = true
            )
        )
        onLand()
    }

    /**
     * 模拟拖拽时的物理效果
     */
    fun calculateDragPhysics(
        currentPosition: Offset,
        dragAmount: Offset,
        velocity: Offset = Offset.Zero
    ): PhysicsState {
        val newPosition = currentPosition + dragAmount
        // 根据速度计算轻微旋转
        val rotationOffset = (velocity.x * 0.5f).coerceIn(-5f, 5f)

        return PhysicsState(
            position = newPosition,
            rotation = rotationOffset,
            scale = 1.05f, // 拖拽时轻微放大
            velocity = velocity
        )
    }

    /**
     * 计算阴影深度
     * 基于元素高度（离桌面的距离）
     */
    fun calculateShadowElevation(
        currentY: Float,
        baseY: Float,
        maxElevation: Float = 16f
    ): androidx.compose.ui.unit.Dp {
        val distance = (currentY - baseY).coerceAtLeast(0f)
        val elevation = 4f + (distance / 100f) * maxElevation
        return elevation.coerceIn(0f, maxElevation).dp
    }

    /**
     * 简单的碰撞检测
     */
    fun checkCollision(
        rect1: Rect,
        rect2: Rect
    ): Boolean {
        return rect1.overlaps(rect2)
    }

    /**
     * 解决 Z-Index 排序
     * 将被移动的元素置于最上层
     */
    fun <T : ZIndexed> resolveZIndex(
        elements: List<T>,
        movedElement: T
    ): List<T> {
        val maxZ = elements.maxOfOrNull { it.zIndex } ?: 0
        return elements.map { element ->
            if (element.id == movedElement.id) {
                @Suppress("UNCHECKED_CAST")
                (element as? MutableZIndexed)?.apply { zIndex = maxZ + 1 } as? T ?: element
            } else {
                element
            }
        }
    }
}

/**
 * 矩形区域 - 用于碰撞检测
 */
data class Rect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    fun overlaps(other: Rect): Boolean {
        return this.left < other.right &&
               this.right > other.left &&
               this.top < other.bottom &&
               this.bottom > other.top
    }
}

/**
 * 有 Z-Index 的接口
 */
interface ZIndexed {
    val id: String
    val zIndex: Int
}

interface MutableZIndexed : ZIndexed {
    override var zIndex: Int
}
