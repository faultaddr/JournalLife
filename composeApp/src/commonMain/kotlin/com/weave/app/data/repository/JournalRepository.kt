package com.weave.app.data.repository

import com.weave.app.data.model.Journal
import com.weave.app.data.model.CoverStyle
import kotlinx.coroutines.flow.Flow

/**
 * 手账本 Repository 接口
 */
interface JournalRepository {
    /**
     * 获取所有手账本
     */
    fun getAllJournals(): Flow<List<Journal>>

    /**
     * 根据 ID 获取手账本
     */
    fun getJournalById(id: String): Flow<Journal?>

    /**
     * 创建手账本
     */
    suspend fun createJournal(title: String, coverStyle: CoverStyle): Journal

    /**
     * 更新手账本
     */
    suspend fun updateJournal(journal: Journal)

    /**
     * 删除手账本
     */
    suspend fun deleteJournal(id: String)
}
