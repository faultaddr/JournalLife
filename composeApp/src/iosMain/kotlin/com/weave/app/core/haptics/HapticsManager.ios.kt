package com.weave.app.core.haptics

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UISelectionFeedbackGenerator

/**
 * iOS 触觉反馈实现
 */
actual class HapticsManager {
    private val lightGenerator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
    private val mediumGenerator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
    private val heavyGenerator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy)
    private val selectionGenerator = UISelectionFeedbackGenerator()

    actual fun performLightImpact() {
        lightGenerator.prepare()
        lightGenerator.impactOccurred()
    }

    actual fun performMediumImpact() {
        mediumGenerator.prepare()
        mediumGenerator.impactOccurred()
    }

    actual fun performHeavyImpact() {
        heavyGenerator.prepare()
        heavyGenerator.impactOccurred()
    }

    actual fun performSelectionChange() {
        selectionGenerator.prepare()
        selectionGenerator.selectionChanged()
    }
}
