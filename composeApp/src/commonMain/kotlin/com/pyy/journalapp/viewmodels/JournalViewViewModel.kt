package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.auth.AuthService
import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.models.Visibility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class JournalViewViewModel(
    private val journalRepository: JournalRepository,
    private val authService: AuthService
) : ViewModel() {
    
    private val _journal = MutableStateFlow<JournalEntry?>(null)
    val journal: StateFlow<JournalEntry?> = _journal
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun loadJournalEntry(journalId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val journalEntry = journalRepository.getJournalEntryById(journalId)
                if (journalEntry != null) {
                    // Load blocks for the journal
                    val blocks = journalRepository.getBlocksByEntryId(journalId)
                    val journalWithBlocks = journalEntry.copy(blocks = blocks)
                    _journal.value = journalWithBlocks
                }
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    fun togglePrivacy() {
        val currentJournal = _journal.value
        if (currentJournal != null) {
            val newVisibility = if (currentJournal.visibility == Visibility.PUBLIC) {
                Visibility.PRIVATE
            } else {
                Visibility.PUBLIC
            }
            
            val updatedJournal = currentJournal.copy(
                visibility = newVisibility,
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            
            _journal.value = updatedJournal
            
            // Update in database
            viewModelScope.launch {
                journalRepository.updateJournalEntry(updatedJournal)
            }
        }
    }
}