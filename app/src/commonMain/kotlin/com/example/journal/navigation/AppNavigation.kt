package com.example.journal.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.bottomTabNavigator
import com.example.journal.screens.*

@Composable
fun AppNavigation() {
    Navigator(Screen.LoginScreen()) { navigator ->
        BottomSheetNavigator {
            TabNavigator(TabScreen.Home) { tabNavigator ->
                bottomTabNavigator(
                    tabs = {
                        listOf(
                            TabScreen.Home,
                            TabScreen.Books,
                            TabScreen.Statistics,
                            TabScreen.Settings
                        )
                    }
                )
            }
        }
    }
}