package com.weave.app.data.repository

import com.weave.app.data.model.Journal
import com.weave.app.data.model.Page
import com.weave.app.data.model.PageElement
import com.weave.app.data.model.Material
import com.weave.app.data.model.MaterialCategory
import kotlinx.coroutines.flow.Flow

/**
 * 手账本仓库接口
 */
interface JournalRepository {
    fun getAllJournals(): Flow<List<Journal>>
    fun getJournalById(id: String): Flow<Journal?>
    suspend fun createJournal(title: String, coverStyle: String): Journal
    suspend fun updateJournal(journal: Journal)
    suspend fun deleteJournal(id: String)
    suspend fun updatePageCount(id: String, count: Int)
}

/**
 * 页面仓库接口
 */
interface PageRepository {
    fun getPagesByJournal(journalId: String): Flow<List<Page>>
    fun getPageById(id: String): Flow<Page?>
    fun getPageByDate(journalId: String, date: Long): Flow<Page?>
    suspend fun createPage(
        journalId: String,
        date: Long,
        paperStyle: String,
        backgroundColor: Long
    ): Page
    suspend fun updatePage(page: Page)
    suspend fun deletePage(id: String)
}

/**
 * 页面元素仓库接口
 */
interface PageElementRepository {
    fun getElementsByPage(pageId: String): Flow<List<PageElement>>
    suspend fun addElement(element: PageElement, pageId: String)
    suspend fun updateElement(element: PageElement, pageId: String)
    suspend fun deleteElement(id: String)
    suspend fun updateZIndex(id: String, zIndex: Int)
}

/**
 * 素材仓库接口
 */
interface MaterialRepository {
    fun getAllMaterials(): Flow<List<Material>>
    fun getMaterialsByCategory(category: MaterialCategory): Flow<List<Material>>
    fun getUnlockedMaterials(): Flow<List<Material>>
    suspend fun unlockMaterial(id: String)
    suspend fun initializeDefaultMaterials()
}
