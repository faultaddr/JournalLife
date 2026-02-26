package com.weave.app

import androidx.compose.ui.window.ComposeUIViewController
import com.weave.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
