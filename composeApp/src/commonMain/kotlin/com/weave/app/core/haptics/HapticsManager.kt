package com.weave.app.core.haptics

/**
 * 触觉反馈管理器 - 预期声明
 */
expect class HapticsManager {
    /**
     * 轻微震动反馈
     */
    fun performLightImpact()

    /**
     * 中等震动反馈
     */
    fun performMediumImpact()

    /**
     * 强烈震动反馈
     */
    fun performHeavyImpact()

    /**
     * 选择变化反馈
     */
    fun performSelectionChange()
}
