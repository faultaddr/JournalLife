package com.pyy.journalapp.utils

import com.pyy.journalapp.models.Block
import com.pyy.journalapp.models.ImageBlock
import com.pyy.journalapp.models.JournalEntry

/**
 * 导出工具类，用于处理手账和书籍的批量导出功能
 */
class ExportManager {

    /**
     * 提取给定日记条目中的所有图片
     * @param journalEntry 期刊条目
     * @return 图片块列表
     */
    fun extractImagesFromJournalEntry(journalEntry: JournalEntry): List<ImageBlock> {
        return journalEntry.blocks
            .filterIsInstance<ImageBlock>()
            .toList()
    }

    /**
     * 提取给定日记条目列表中的所有图片
     * @param journalEntries 期刊条目列表
     * @return 图片块列表
     */
    fun extractImagesFromJournalEntries(journalEntries: List<JournalEntry>): List<ImageBlock> {
        return journalEntries.flatMap { entry ->
            extractImagesFromJournalEntry(entry)
        }.distinctBy { it.imageId }
    }

    /**
     * 提取给定块列表中的所有图片
     * @param blocks 内容块列表
     * @return 图片块列表
     */
    fun extractImagesFromBlocks(blocks: List<Block>): List<ImageBlock> {
        return blocks
            .filterIsInstance<ImageBlock>()
            .toList()
    }
}