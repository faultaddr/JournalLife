package com.weave.app.data.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

/**
 * 手账本
 */
@Serializable
data class Journal(
    val id: String,
    val title: String,
    val coverStyle: CoverStyle,
    val createdAt: Long,
    val updatedAt: Long,
    val pageCount: Int = 0,
    val thickness: Float = 10f + pageCount * 0.5f  // 用于3D书架显示
)

/**
 * 封面样式
 */
@Serializable
enum class CoverStyle {
    LEATHER_BROWN,
    LEATHER_RED,
    LEATHER_BLUE,
    LEATHER_GREEN,
    FABRIC_BEIGE,
    FABRIC_GRAY,
    CANVAS_KRAFT,
    VINTAGE_MAP
}

/**
 * 页面
 */
@Serializable
data class Page(
    val id: String,
    val journalId: String,
    val date: Long,
    val paperStyle: PaperStyle,
    val backgroundColor: Long = PaperColor.WHITE.value,
    val elements: List<PageElement> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * 纸张样式
 */
@Serializable
enum class PaperStyle {
    PLAIN,      // 空白
    RULED,      // 横线
    GRID,       // 网格
    DOT,        // 点阵
    KRAFT,      // 牛皮纸
    WATERCOLOR  // 水彩纸
}

/**
 * 纸张颜色
 */
enum class PaperColor(val value: Long) {
    WHITE(0xFFFDFCF8),
    CREAM(0xFFF5F0E1),
    KRAFT(0xFFD4C5A9),
    PINK(0xFFFAE5E5),
    BLUE(0xFFE5F0FA),
    GREEN(0xFFE5FAE8),
    YELLOW(0xFFFAF5E5)
}

/**
 * 页面元素 - 密封类表示不同类型的元素
 */
@Serializable
sealed class PageElement {
    abstract val id: String
    abstract val positionX: Float
    abstract val positionY: Float
    abstract val rotation: Float
    abstract val scale: Float
    abstract val zIndex: Int

    /**
     * 获取位置偏移量
     */
    fun getPosition(): Offset = Offset(positionX, positionY)
}

/**
 * 贴纸元素
 */
@Serializable
data class StickerElement(
    override val id: String,
    override val positionX: Float,
    override val positionY: Float,
    override val rotation: Float = 0f,
    override val scale: Float = 1f,
    override val zIndex: Int = 0,
    val stickerId: String,
    val wearLevel: Float = 0f,  // 0.0-1.0，模拟使用损耗
    val flipX: Boolean = false, // 是否水平翻转
    val flipY: Boolean = false  // 是否垂直翻转
) : PageElement()

/**
 * 胶带元素
 */
@Serializable
data class TapeElement(
    override val id: String,
    override val positionX: Float,
    override val positionY: Float,
    override val rotation: Float = 0f,
    override val scale: Float = 1f,
    override val zIndex: Int = 0,
    val tapeId: String,
    val width: Float = 100f,
    val height: Float = 20f,
    val ripPattern: List<OffsetSerializable> = emptyList()  // 撕裂不规则边缘
) : PageElement()

/**
 * 可序列化的 Offset
 */
@Serializable
data class OffsetSerializable(
    val x: Float,
    val y: Float
) {
    fun toOffset(): Offset = Offset(x, y)
}

fun Offset.toSerializable(): OffsetSerializable = OffsetSerializable(x, y)

/**
 * 照片元素
 */
@Serializable
data class PhotoElement(
    override val id: String,
    override val positionX: Float,
    override val positionY: Float,
    override val rotation: Float = 0f,
    override val scale: Float = 1f,
    override val zIndex: Int = 0,
    val photoUri: String,
    val polaroidStyle: Boolean = true,
    val developProgress: Float = 0f,  // 显影进度 0.0-1.0
    val caption: String = ""          // 照片说明文字
) : PageElement()

/**
 * 文字元素
 */
@Serializable
data class TextElement(
    override val id: String,
    override val positionX: Float,
    override val positionY: Float,
    override val rotation: Float = 0f,
    override val scale: Float = 1f,
    override val zIndex: Int = 0,
    val content: String,
    val penType: PenType = PenType.FOUNTAIN,
    val inkColor: Long = 0xFF000000,  // 存储为 Long 以便序列化
    val fontSize: Float = 16f
) : PageElement() {
    fun getInkColor(): Color = Color(inkColor)
}

/**
 * 笔类型
 */
@Serializable
enum class PenType {
    FOUNTAIN,   // 钢笔
    BALLPOINT,  // 圆珠笔
    MARKER,     // 马克笔
    BRUSH,      // 毛笔
    PENCIL      // 铅笔
}

/**
 * 素材（贴纸、胶带等）
 */
@Serializable
data class Material(
    val id: String,
    val name: String,
    val category: MaterialCategory,
    val resourcePath: String,
    val rarity: Rarity = Rarity.COMMON,
    val isUnlocked: Boolean = false,
    val unlockCondition: String? = null,
    val acquiredAt: Long? = null
)

/**
 * 素材分类
 */
@Serializable
enum class MaterialCategory {
    STICKER,    // 贴纸
    TAPE,       // 胶带
    PAPER,      // 纸张
    FRAME,      // 相框
    PEN,        // 笔
    STAMP       // 印章
}

/**
 * 稀有度
 */
@Serializable
enum class Rarity {
    COMMON,     // 普通
    RARE,       // 稀有
    EPIC,       // 史诗
    LEGENDARY   // 传说
}
