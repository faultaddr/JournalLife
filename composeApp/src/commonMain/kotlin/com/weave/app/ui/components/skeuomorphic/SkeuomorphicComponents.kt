package com.weave.app.ui.components.skeuomorphic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.weave.app.ui.theme.WeaveColors

/**
 * 拟物化按钮 - Neumorphism 风格
 *
 * @param onClick 点击回调
 * @param modifier 修饰符
 * @param shape 形状
 * @param elevation 正常状态下的阴影高度
 * @param pressedElevation 按下时的阴影高度
 * @param backgroundColor 背景色
 * @param content 内容
 */
@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 8.dp,
    pressedElevation: Dp = 4.dp,
    backgroundColor: Color = WeaveColors.PaperWhite,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) pressedElevation.value else elevation.value,
        label = "elevation"
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = animatedElevation.dp,
                shape = shape,
                clip = false,
                ambientColor = if (isPressed) WeaveColors.ShadowDark else WeaveColors.ShadowLight,
                spotColor = if (isPressed) WeaveColors.ShadowLight else WeaveColors.ShadowDark
            )
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}

/**
 * 纸张组件 - 拟物化纸张效果
 *
 * @param modifier 修饰符
 * @param shape 形状
 * @param elevation 阴影高度
 * @param backgroundColor 背景色
 * @param content 内容
 */
@Composable
fun PaperSheet(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    elevation: Dp = 4.dp,
    backgroundColor: Color = WeaveColors.PaperWhite,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                clip = false,
                ambientColor = WeaveColors.ShadowDark,
                spotColor = WeaveColors.ShadowDarker
            )
            .background(backgroundColor, shape),
        content = content
    )
}

/**
 * 工具箱按钮 - 模拟实物工具箱
 */
@Composable
fun ToolBoxButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    NeumorphicButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = 6.dp,
        pressedElevation = 2.dp,
        backgroundColor = WeaveColors.ToolBox
    ) {
        Box(
            modifier = Modifier.padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}

/**
 * 笔袋按钮 - 模拟笔袋
 */
@Composable
fun PenCaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    NeumorphicButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp, 8.dp, 24.dp, 8.dp),
        elevation = 6.dp,
        pressedElevation = 2.dp,
        backgroundColor = WeaveColors.ToolCase
    ) {
        Box(
            modifier = Modifier.padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}

/**
 * 抽屉按钮 - 模拟抽屉
 */
@Composable
fun DrawerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    NeumorphicButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        pressedElevation = 1.dp,
        backgroundColor = WeaveColors.ToolDrawer
    ) {
        Box(
            modifier = Modifier.padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}

/**
 * 贴纸卡片 - 带轻微卷边效果
 */
@Composable
fun StickerCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(4.dp),
                clip = false
            )
            .background(WeaveColors.PaperWhite, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
        content = content
    )
}
