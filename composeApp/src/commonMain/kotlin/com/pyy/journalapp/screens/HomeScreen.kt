package com.pyy.journalapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.Book
import com.pyy.journalapp.ui.theme.JournalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    onAddBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("我的日记本") },
                    actions = {
                        IconButton(onClick = { /* Navigate to settings */ }) {
                            Text("⚙️") // 设置图标
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onAddBookClick,
                    icon = { Text("➕") }, // Plus icon as text
                    text = { Text("新建书册") }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                if (books.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "还没有日记本\n点击下方按钮创建一个",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(books) { book ->
                        BookItem(
                            book = book,
                            onClick = { onBookClick(book) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1
                )
                Text("📖", style = MaterialTheme.typography.bodyLarge) // Book emoji
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (book.description != null) {
                Text(
                    text = book.description!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${book.visibilityDefault.name.lowercase()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (book.visibilityDefault == com.pyy.journalapp.models.Visibility.PUBLIC)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "更新: ${formatDate(book.updatedAt)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Helper function to format date
private fun formatDate(dateTime: kotlinx.datetime.LocalDateTime): String {
    return "${dateTime.monthNumber}/${dateTime.dayOfMonth}/${dateTime.year}"
}