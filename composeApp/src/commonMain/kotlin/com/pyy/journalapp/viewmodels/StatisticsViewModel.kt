package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.auth.AuthService
import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.screens.StatisticsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val journalRepository: JournalRepository,
    private val authService: AuthService
) : ViewModel() {
    
    private val _stats = MutableStateFlow(StatisticsData(
        totalEntries = 0,
        totalWords = 0,
        totalImages = 0,
        writingFrequency = emptyMap(),
        wordsPerWeek = emptyMap(),
        topTags = emptyMap()
    ))
    val stats: StateFlow<StatisticsData> = _stats
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun loadStats() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    val allJournals = mutableListOf<JournalEntry>()
                    
                    // Get all books for the user
                    val books = journalRepository.getBooksByOwnerId(currentUser.id)
                    
                    // Get all journal entries for each book
                    for (book in books) {
                        val journals = journalRepository.getJournalEntriesByBookId(book.id)
                        allJournals.addAll(journals)
                    }
                    
                    // Calculate statistics
                    val totalEntries = allJournals.size
                    val totalWords = allJournals.sumOf { it.metricsCache.wordCount }
                    val totalImages = allJournals.sumOf { it.metricsCache.imageCount }
                    
                    // Calculate writing frequency (by date)
                    val writingFrequency = allJournals.groupingBy {
                        "${it.createdAt.year}-${it.createdAt.monthNumber}-${it.createdAt.dayOfMonth}"
                    }.eachCount()
                    
                    // Calculate words per week
                    val wordsPerWeek = allJournals.groupingBy {
                        "${it.createdAt.year}-W${getWeekNumber(it.createdAt)}"
                    }.fold(0) { acc, journal ->
                        acc + journal.metricsCache.wordCount
                    }
                    
                    // Calculate top tags
                    val allTags = allJournals.flatMap { it.tags }
                    val topTags = allTags.groupingBy { it }.eachCount()
                        .toList()
                        .sortedByDescending { (_, count) -> count }
                        .take(10)
                        .toMap()
                    
                    val statsData = StatisticsData(
                        totalEntries = totalEntries,
                        totalWords = totalWords,
                        totalImages = totalImages,
                        writingFrequency = writingFrequency,
                        wordsPerWeek = wordsPerWeek,
                        topTags = topTags
                    )
                    
                    _stats.value = statsData
                }
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    private fun getWeekNumber(dateTime: kotlinx.datetime.LocalDateTime): Int {
        // Simple calculation for week number (1-52)
        val dayOfYear = dateTime.dayOfYear
        return ((dayOfYear - 1) / 7) + 1
    }
}