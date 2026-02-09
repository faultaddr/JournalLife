package com.example.journal.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.journal.models.*

@Composable
fun JournalList() {
    val navigator = LocalNavigator.currentOrThrow
    
    // Sample data for demonstration
    val journals = listOf(
        JournalEntry(
            id = "1",
            ownerId = "user1",
            bookId = "book1",
            title = "Travel Journal - Day 1",
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            visibility = Visibility.PUBLIC,
            tags = listOf("travel", "vacation"),
            metricsCache = MetricsCache(wordCount = 250, imageCount = 3)
        ),
        JournalEntry(
            id = "2",
            ownerId = "user1",
            bookId = "book1",
            title = "Learning Notes - Compose",
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            visibility = Visibility.PRIVATE,
            tags = listOf("learning", "tech"),
            metricsCache = MetricsCache(wordCount = 500, imageCount = 1)
        )
    )
    
    LazyColumn {
        items(journals) { journal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navigator.push(JournalDetailScreen(journal.id)) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = journal.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Icon(
                            imageVector = if (journal.visibility == Visibility.PUBLIC) Icons.Default.Public else Icons.Default.Lock,
                            contentDescription = if (journal.visibility == Visibility.PUBLIC) "Public" else "Private",
                            tint = if (journal.visibility == Visibility.PUBLIC) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${journal.metricsCache.wordCount} words, ${journal.metricsCache.imageCount} images",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (journal.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row {
                            journal.tags.forEach { tag ->
                                Chip(
                                    modifier = Modifier.padding(end = 8.dp),
                                    onClick = { /* Handle tag click */ }
                                ) {
                                    Text(text = tag, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookList() {
    val navigator = LocalNavigator.currentOrThrow
    
    // Sample data for demonstration
    val books = listOf(
        Book(
            id = "book1",
            ownerId = "user1",
            title = "Travel Adventures",
            description = "Collection of travel experiences",
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            visibilityDefault = Visibility.PUBLIC
        ),
        Book(
            id = "book2",
            ownerId = "user1",
            title = "Learning Notes",
            description = "Study materials and notes",
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            visibilityDefault = Visibility.PRIVATE
        )
    )
    
    LazyColumn {
        items(books) { book ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navigator.push(BookDetailScreen(book.id)) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (book.description != null) {
                        Text(
                            text = book.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Text(
                            text = if (book.visibilityDefault == Visibility.PUBLIC) "Public" else "Private",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalContent(journalId: String) {
    // Sample data for demonstration
    val journal = JournalEntry(
        id = journalId,
        ownerId = "user1",
        bookId = "book1",
        title = "Sample Journal Entry",
        createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
        updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
        visibility = Visibility.PUBLIC,
        tags = listOf("sample", "demo"),
        blocks = listOf(
            TextBlock(
                id = "block1",
                createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                orderIndex = 0,
                text = "This is a sample journal entry demonstrating the app's functionality.",
                style = TextStyle(bold = true)
            ),
            TextBlock(
                id = "block2",
                createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                orderIndex = 1,
                text = "Here is some additional content in the journal. You can add text, images, and other elements to create rich journal entries."
            ),
            ImageBlock(
                id = "block3",
                createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                orderIndex = 2,
                imageId = "image1",
                caption = "Sample image in the journal"
            )
        )
    )
    
    LazyColumn {
        item {
            Text(
                text = journal.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        items(journal.blocks) { block ->
            when (block) {
                is TextBlock -> {
                    Text(
                        text = block.text,
                        style = if (block.style.bold) MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                is ImageBlock -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Image Placeholder",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            if (block.caption != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = block.caption,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Unsupported block type",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BookContent(bookId: String) {
    // Sample data for demonstration
    val book = Book(
        id = bookId,
        ownerId = "user1",
        title = "Sample Book",
        description = "This is a sample book showing how the app organizes journals",
        createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
        updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
        visibilityDefault = Visibility.PUBLIC
    )
    
    val journals = listOf(
        JournalEntry(
            id = "j1",
            ownerId = "user1",
            bookId = bookId,
            title = "First Journal in Book",
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            visibility = Visibility.PUBLIC
        ),
        JournalEntry(
            id = "j2",
            ownerId = "user1",
            bookId = bookId,
            title = "Second Journal in Book",
            createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
            visibility = Visibility.PRIVATE
        )
    )
    
    Column {
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (book.description != null) {
            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        LazyColumn {
            items(journals) { journal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = journal.title,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Icon(
                            imageVector = if (journal.visibility == Visibility.PUBLIC) Icons.Default.Public else Icons.Default.Lock,
                            contentDescription = if (journal.visibility == Visibility.PUBLIC) "Public" else "Private",
                            tint = if (journal.visibility == Visibility.PUBLIC) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticsView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Writing Frequency",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Placeholder for chart
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Chart Visualization Would Go Here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Text(
            text = "Word Count Distribution",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Placeholder for chart
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Chart Visualization Would Go Here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SettingsView() {
    LazyColumn {
        item {
            Text(
                text = "Account Settings",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        
        item {
            ListItem(
                headlineContent = { Text("Privacy Settings") },
                supportingContent = { Text("Manage your privacy preferences") },
                trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
            )
        }
        
        item {
            ListItem(
                headlineContent = { Text("Export Options") },
                supportingContent = { Text("Configure export formats and options") },
                trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
            )
        }
        
        item {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        
        item {
            var darkTheme by remember { mutableStateOf(false) }
            
            SwitchPreference(
                checked = darkTheme,
                onCheckedChange = { darkTheme = it },
                title = "Dark Theme",
                description = "Toggle dark/light theme"
            )
        }
        
        item {
            var language by remember { mutableStateOf("English") }
            
            ListPreference(
                value = language,
                onValueChange = { language = it },
                title = "Language",
                description = "Select app language",
                entries = mapOf(
                    "en" to "English",
                    "zh" to "中文",
                    "es" to "Español"
                )
            )
        }
    }
}

@Composable
fun SwitchPreference(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    description: String
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}

@Composable
fun ListPreference(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    description: String,
    entries: Map<String, String>
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = { Text(description) },
            trailingContent = {
                Box {
                    Text(value)
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        entries.values.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    onValueChange(selectionOption)
                                    expanded = false
                                },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}