package com.pyy.journalapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ==================== 温暖日记主题配色 ====================

// 主色调 - 温暖的琥珀/日落橙
val WarmAmber = Color(0xFFE8A87C)
val WarmAmberLight = Color(0xFFF6D365)
val WarmAmberDark = Color(0xFFD4895A)

// 次要色 - 柔和的莫兰迪粉
val SoftRose = Color(0xFFC38D9E)
val SoftRoseLight = Color(0xFFE8B4C4)

// 第三色 - 清新的薄荷绿
val MintGreen = Color(0xFF85CDCA)
val MintGreenLight = Color(0xFFA8E6E3)

// 背景色 - 温暖的米白/奶油色
val CreamBackground = Color(0xFFFFFCF8)
val WarmGray = Color(0xFFF5F0EB)
val SoftBeige = Color(0xFFFAF6F1)

// 文字颜色
val TextPrimary = Color(0xFF2D2A26)
val TextSecondary = Color(0xFF6B6560)
val TextTertiary = Color(0xFF9A9590)

// 渐变配色方案（供书册卡片使用）
val SunsetGradient = listOf(Color(0xFFF6D365), Color(0xFFFDA085))
val OceanGradient = listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
val SakuraGradient = listOf(Color(0xFFFF9A9E), Color(0xFFFECFEF))
val ForestGradient = listOf(Color(0xFF43E97B), Color(0xFF38F9D7))
val VioletGradient = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
val CoffeeGradient = listOf(Color(0xFFA1887F), Color(0xFF8D6E63))
val TwilightGradient = listOf(Color(0xFFFA709A), Color(0xFFFEE140))
val BerryGradient = listOf(Color(0xFF667EEA), Color(0xFF764BA2))

// 暗色主题配色
val DarkBackground = Color(0xFF1A1816)
val DarkSurface = Color(0xFF2D2A26)
val DarkSurfaceVariant = Color(0xFF3D3A36)

// ==================== 颜色方案 ====================

private val LightColorScheme = lightColorScheme(
    primary = WarmAmber,
    onPrimary = Color.White,
    primaryContainer = WarmAmberLight,
    onPrimaryContainer = Color(0xFF4A3728),

    secondary = SoftRose,
    onSecondary = Color.White,
    secondaryContainer = SoftRoseLight,
    onSecondaryContainer = Color(0xFF4A2F38),

    tertiary = MintGreen,
    onTertiary = Color.White,
    tertiaryContainer = MintGreenLight,
    onTertiaryContainer = Color(0xFF2A4A48),

    background = CreamBackground,
    onBackground = TextPrimary,

    surface = Color.White,
    onSurface = TextPrimary,
    surfaceVariant = WarmGray,
    onSurfaceVariant = TextSecondary,

    outline = Color(0xFFE0DCD6),
    outlineVariant = Color(0xFFEAE6E1),

    error = Color(0xFFE57373),
    onError = Color.White,

    scrim = Color(0x80000000)
)

private val DarkColorScheme = darkColorScheme(
    primary = WarmAmberLight,
    onPrimary = Color(0xFF4A3728),
    primaryContainer = WarmAmberDark,
    onPrimaryContainer = Color.White,

    secondary = SoftRoseLight,
    onSecondary = Color(0xFF4A2F38),
    secondaryContainer = SoftRose,
    onSecondaryContainer = Color.White,

    tertiary = MintGreenLight,
    onTertiary = Color(0xFF2A4A48),
    tertiaryContainer = MintGreen,
    onTertiaryContainer = Color.White,

    background = DarkBackground,
    onBackground = Color(0xFFE6E1DC),

    surface = DarkSurface,
    onSurface = Color(0xFFE6E1DC),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB0ABA6),

    outline = Color(0xFF4D4A46),
    outlineVariant = Color(0xFF3D3A36),

    error = Color(0xFFEF9A9A),
    onError = Color(0xFF3D1A1A),

    scrim = Color(0xCC000000)
)

// ==================== 自定义Typography ====================

val JournalTypography = Typography(
    // 大标题 - 用于欢迎语
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    // 中等标题 - 用于书册标题
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    // 小标题 - 用于区域标题
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // 大标题 - 用于卡片标题
    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    // 中等标题
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    // 小标题
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    // 大号正文
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // 中等正文
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    // 小正文
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    // 大标签 - 用于统计数字
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    // 中等标签
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    // 小标签
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// ==================== Theme Composable ====================

@Composable
fun JournalAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = JournalTypography,
        content = content
    )
}
