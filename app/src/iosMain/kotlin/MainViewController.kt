package com.example.journal

import androidx.compose.ui.window.ComposeUIViewController
import com.example.journal.di.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

fun MainViewController() = ComposeUIViewController { 
    MainApp() 
}