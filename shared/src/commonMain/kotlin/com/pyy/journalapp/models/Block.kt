package com.pyy.journalapp.models

import kotlinx.datetime.LocalDateTime

sealed class Block(
    open val id: String,
    val type: BlockType,
    open val createdAt: LocalDateTime,
    open val updatedAt: LocalDateTime,
    open val orderIndex: Int
)

enum class BlockType {
    TEXT, IMAGE, TODO, DIVIDER, QUOTE, HEADING
}

data class TextBlock(
    override val id: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
    override val orderIndex: Int,
    val text: String,
    val style: TextStyle = TextStyle(),
    val format: TextFormat = TextFormat.PLAIN
) : Block(id, BlockType.TEXT, createdAt, updatedAt, orderIndex)

data class TextStyle(
    val bold: Boolean = false,
    val italic: Boolean = false,
    val fontSize: Int? = null,
    val color: String? = null,
    val alignment: TextAlignment? = null
)

enum class TextFormat {
    PLAIN, MARKDOWN_LITE
}

enum class TextAlignment {
    LEFT, CENTER, RIGHT, JUSTIFY
}

data class ImageBlock(
    override val id: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
    override val orderIndex: Int,
    val imageId: String,
    val caption: String? = null,
    val layout: ImageLayout = ImageLayout.INLINE,
    val crop: ImageCrop? = null
) : Block(id, BlockType.IMAGE, createdAt, updatedAt, orderIndex)

enum class ImageLayout {
    INLINE, FULL_WIDTH
}

data class ImageCrop(
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float
)

data class TodoBlock(
    override val id: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
    override val orderIndex: Int,
    val text: String,
    val completed: Boolean = false
) : Block(id, BlockType.TODO, createdAt, updatedAt, orderIndex)

data class DividerBlock(
    override val id: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
    override val orderIndex: Int
) : Block(id, BlockType.DIVIDER, createdAt, updatedAt, orderIndex)

data class QuoteBlock(
    override val id: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
    override val orderIndex: Int,
    val text: String,
    val author: String? = null
) : Block(id, BlockType.QUOTE, createdAt, updatedAt, orderIndex)

data class HeadingBlock(
    override val id: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
    override val orderIndex: Int,
    val text: String,
    val level: Int = 1 // 1-6 like HTML headings
) : Block(id, BlockType.HEADING, createdAt, updatedAt, orderIndex)