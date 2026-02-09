package com.example.journal.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.journal.screens.*

sealed class TabScreen(
    private val title: String,
    private val icon: @Composable () -> Unit,
    val screen: @Composable () -> Unit
) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = icon()
            return TabOptions(0u, title) { icon }
        }

    object Home : TabScreen(
        title = "Home",
        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
        screen = { HomeScreen() }
    )

    object Books : TabScreen(
        title = "Books",
        icon = { Icon(Icons.Default.MenuBook, contentDescription = "Books") },
        screen = { BooksScreen() }
    )

    object Statistics : TabScreen(
        title = "Stats",
        icon = { Icon(Icons.Default.BarChart, contentDescription = "Statistics") },
        screen = { StatisticsScreen() }
    )

    object Settings : TabScreen(
        title = "Settings",
        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
        screen = { SettingsScreen() }
    )
}