package com.weave.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 织忆主题颜色
 */
object WeaveColors {
    // 桌面背景 - 木质纹理色
    val WoodLight = Color(0xFFA0826D)
    val WoodMedium = Color(0xFF8B7355)
    val WoodDark = Color(0xFF6B5344)

    // 布艺纹理
    val FabricBeige = Color(0xFFF5F5DC)
    val FabricCream = Color(0xFFFAF0E6)
    val FabricGray = Color(0xFFE8E8E8)

    // 纸张颜色
    val PaperWhite = Color(0xFFFDFCF8)
    val PaperCream = Color(0xFFF5F0E1)
    val PaperKraft = Color(0xFFD4C5A9)
    val PaperPink = Color(0xFFFAE5E5)
    val PaperBlue = Color(0xFFE5F0FA)
    val PaperGreen = Color(0xFFE5FAE8)
    val PaperYellow = Color(0xFFFAF5E5)

    // 阴影 - 用于拟物化效果
    val ShadowLight = Color(0x40FFFFFF)  // 高光
    val ShadowDark = Color(0x40000000)   // 阴影
    val ShadowDarker = Color(0x80000000) // 深色阴影

    // Neumorphism 效果色
    val NeumorphismLight = Color(0xFFFFFFFF)
    val NeumorphismDark = Color(0xFFB8B9BE)

    // 工具栏颜色
    val ToolCase = Color(0xFF5D4037)
    val ToolBox = Color(0xFF795548)
    val ToolDrawer = Color(0xFF8D6E63)

    // 强调色
    val AccentGold = Color(0xFFD4AF37)
    val AccentCopper = Color(0xFFB87333)
    val AccentSilver = Color(0xFFC0C0C0)

    // 墨迹颜色
    val InkBlack = Color(0xFF000000)
    val InkBlue = Color(0xFF003366)
    val InkRed = Color(0xFF8B0000)
    val InkGreen = Color(0xFF006400)
    val InkPurple = Color(0xFF4B0082)
    val InkBrown = Color(0xFF8B4513)

    // 根据纸张样式获取颜色
    fun getPaperColor(style: com.weave.app.data.model.PaperStyle): Color {
        return when (style) {
            com.weave.app.data.model.PaperStyle.PLAIN -> PaperWhite
            com.weave.app.data.model.PaperStyle.RULED -> PaperCream
            com.weave.app.data.model.PaperStyle.GRID -> PaperWhite
            com.weave.app.data.model.PaperStyle.DOT -> PaperCream
            com.weave.app.data.model.PaperStyle.KRAFT -> PaperKraft
            com.weave.app.data.model.PaperStyle.WATERCOLOR -> PaperWhite
        }
    }
}
