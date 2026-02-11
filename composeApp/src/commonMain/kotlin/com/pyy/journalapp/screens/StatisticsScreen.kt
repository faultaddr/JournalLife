package com.pyy.journalapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.ui.theme.JournalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    stats: StatisticsData,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Statistics") },
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
                // Writing Frequency Chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Writing Frequency",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Placeholder for chart - in a real app, we'd use a charting library
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Line Chart: Entries per Day/Week/Month",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                // Word Count Chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Words Written",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Placeholder for chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Bar Chart: Words per Week",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                // Tag Distribution Chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Tag Distribution",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Placeholder for chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pie Chart: Top Tags",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                // Summary Cards
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SummaryCard(
                        title = "Total Entries",
                        value = stats.totalEntries.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    
                    SummaryCard(
                        title = "Total Words",
                        value = stats.totalWords.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                SummaryCard(
                    title = "Total Images",
                    value = stats.totalImages.toString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Data class for statistics
data class StatisticsData(
    val totalEntries: Int,
    val totalWords: Int,
    val totalImages: Int,
    val writingFrequency: Map<String, Int>, // Date to count map
    val wordsPerWeek: Map<String, Int>, // Week to word count map
    val topTags: Map<String, Int> // Tag to count map
)