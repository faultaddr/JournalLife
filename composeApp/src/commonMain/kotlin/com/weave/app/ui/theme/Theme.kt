package com.weave.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 织忆应用主题
 */
private val LightColorScheme = lightColorScheme(
    primary = WeaveColors.WoodMedium,
    onPrimary = WeaveColors.PaperWhite,
    secondary = WeaveColors.AccentGold,
    onSecondary = WeaveColors.InkBlack,
    tertiary = WeaveColors.AccentCopper,
    onTertiary = WeaveColors.PaperWhite,
    background = WeaveColors.WoodMedium,
    onBackground = WeaveColors.InkBlack,
    surface = WeaveColors.PaperCream,
    onSurface = WeaveColors.InkBlack,
    surfaceVariant = WeaveColors.PaperWhite,
    onSurfaceVariant = WeaveColors.InkBlack,
    outline = WeaveColors.WoodDark
)

private val DarkColorScheme = darkColorScheme(
    primary = WeaveColors.WoodDark,
    onPrimary = WeaveColors.PaperWhite,
    secondary = WeaveColors.AccentGold,
    onSecondary = WeaveColors.PaperWhite,
    tertiary = WeaveColors.AccentCopper,
    onTertiary = WeaveColors.PaperWhite,
    background = WeaveColors.WoodDark,
    onBackground = WeaveColors.PaperCream,
    surface = WeaveColors.WoodMedium,
    onSurface = WeaveColors.PaperWhite,
    surfaceVariant = WeaveColors.WoodLight,
    onSurfaceVariant = WeaveColors.PaperCream,
    outline = WeaveColors.FabricBeige
)

/**
 * 织忆字体样式 - 手写风格
 */
val WeaveTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Light,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Light,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)

/**
 * 织忆主题
 */
@Composable
fun WeaveTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WeaveTypography,
        content = content
    )
}
