package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.auth.AuthService
import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.utils.IdGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
class BookDetailViewModel(
    private val journalRepository: JournalRepository,
    private val authService: AuthService
) : ViewModel() {
    
    private val _journals = MutableStateFlow<List<JournalEntry>>(emptyList())
    val journals: StateFlow<List<JournalEntry>> = _journals
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun loadJournalsForBook(bookId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val journals = journalRepository.getJournalEntriesByBookId(bookId)
                _journals.value = journals
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    fun addJournalEntry(bookId: String, title: String) {
        viewModelScope.launch {
            try {
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    val newJournal = com.pyy.journalapp.models.JournalEntry(
                        id = IdGenerator.generateId(),
                        ownerId = currentUser.id,
                        bookId = bookId,
                        title = title,
                        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    )

                    journalRepository.createJournalEntry(newJournal)
                    loadJournalsForBook(bookId) // Refresh the list
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}