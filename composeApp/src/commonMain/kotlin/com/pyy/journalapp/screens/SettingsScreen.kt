package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.Theme
import com.pyy.journalapp.models.Visibility
import com.pyy.journalapp.ui.theme.JournalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: Theme,
    currentPrivacyDefault: Visibility,
    onThemeChange: (Theme) -> Unit,
    onPrivacyDefaultChange: (Visibility) -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }
    var selectedPrivacyDefault by remember { mutableStateOf(currentPrivacyDefault) }
    
    LaunchedEffect(currentTheme, currentPrivacyDefault) {
        selectedTheme = currentTheme
        selectedPrivacyDefault = currentPrivacyDefault
    }
    
    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Text("<-") // Fallback back icon
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Theme Settings
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Theme",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Theme selection options
                        Theme.values().forEach { theme ->
                            FilterChip(
                                selected = selectedTheme == theme,
                                onClick = {
                                    selectedTheme = theme
                                    onThemeChange(theme)
                                },
                                label = { Text(theme.name) }
                            )
                        }
                    }
                }
                
                // Privacy Default Settings
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Default Privacy",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Privacy default options
                        Visibility.values().forEach { visibility ->
                            FilterChip(
                                selected = selectedPrivacyDefault == visibility,
                                onClick = {
                                    selectedPrivacyDefault = visibility
                                    onPrivacyDefaultChange(visibility)
                                },
                                label = { Text(visibility.name) }
                            )
                        }
                    }
                }
                
                // Export Options
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Export Options",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Export quality options
                        listOf("High Quality", "Medium Quality", "Low Quality").forEach { quality ->
                            FilterChip(
                                selected = quality == "Medium Quality", // Default selection
                                onClick = { /* Handle quality selection */ },
                                label = { Text(quality) }
                            )
                        }
                    }
                }
                
                // Account Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Account",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Button(
                            onClick = onLogoutClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Text("🚪 ") // Fallback logout icon
                            Text("Logout")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}