package com.example.journal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.journal.di.appModule
import com.example.journal.navigation.AppNavigation
import com.example.journal.ui.theme.JournalTheme
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin

@Composable
fun MainApp() {
    KoinContext {
        JournalTheme {
            AppNavigation()
        }
    }
}