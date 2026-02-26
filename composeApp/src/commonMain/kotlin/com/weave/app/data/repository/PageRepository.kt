package com.weave.app.data.repository

import com.weave.app.data.model.Page
import com.weave.app.data.model.PaperStyle
import kotlinx.coroutines.flow.Flow

/**
 * 页面 Repository 接口
 */
interface PageRepository {
    /**
     * 根据手账本 ID 获取所有页面
     */
    fun getPagesByJournal(journalId: String): Flow<List<Page>>

    /**
     * 根据日期获取页面
     */
    fun getPageByDate(journalId: String, date: Long): Flow<Page?>

    /**
     * 根据 ID 获取页面
     */
    fun getPageById(id: String): Flow<Page?>

    /**
     * 创建页面
     */
    suspend fun createPage(journalId: String, paperStyle: PaperStyle, date: Long): Page

    /**
     * 更新页面
     */
    suspend fun updatePage(page: Page)

    /**
     * 删除页面
     */
    suspend fun deletePage(id: String)
}
