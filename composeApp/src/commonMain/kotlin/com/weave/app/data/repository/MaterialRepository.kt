package com.weave.app.data.repository

import com.weave.app.data.model.Material
import com.weave.app.data.model.MaterialCategory
import kotlinx.coroutines.flow.Flow

/**
 * 素材 Repository 接口
 */
interface MaterialRepository {
    /**
     * 获取所有素材
     */
    fun getAllMaterials(): Flow<List<Material>>

    /**
     * 根据分类获取素材
     */
    fun getMaterialsByCategory(category: MaterialCategory): Flow<List<Material>>

    /**
     * 获取已解锁的素材
     */
    fun getUnlockedMaterials(): Flow<List<Material>>

    /**
     * 解锁素材
     */
    suspend fun unlockMaterial(id: String)

    /**
     * 检查每日奖励
     */
    suspend fun checkDailyRewards(): List<Material>

    /**
     * 初始化默认素材
     */
    suspend fun initDefaultMaterials()
}
