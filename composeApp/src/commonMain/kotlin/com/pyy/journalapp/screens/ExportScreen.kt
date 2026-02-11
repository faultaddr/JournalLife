package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.ui.theme.JournalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    onBackClick: () -> Unit,
    onExportImageClick: () -> Unit,
    onExportTextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Export Journal") },
                )
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose export format:",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                // Export as Image option
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Export as Image",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Create a long image with your journal content",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = onExportImageClick,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Export")
                        }
                    }
                }
                
                // Export as Text option
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Export as Text",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Save your journal as a text file (Markdown format)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = onExportTextClick,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Export")
                        }
                    }
                }
                
                // Additional export options could be added here
            }
        }
    }
}