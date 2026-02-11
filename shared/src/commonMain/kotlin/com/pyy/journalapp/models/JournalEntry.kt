package com.pyy.journalapp.models

import kotlinx.datetime.LocalDateTime

data class JournalEntry(
    val id: String,
    val ownerId: String,
    val bookId: String,
    val title: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val visibility: Visibility = Visibility.PRIVATE,
    val tags: List<String> = emptyList(),
    val blocks: List<Block> = emptyList(),
    val metricsCache: MetricsCache = MetricsCache()
)

data class MetricsCache(
    val wordCount: Int = 0,
    val imageCount: Int = 0
)