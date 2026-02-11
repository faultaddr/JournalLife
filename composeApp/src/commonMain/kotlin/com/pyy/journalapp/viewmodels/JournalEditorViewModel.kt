package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.auth.AuthService
import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.*
import com.pyy.journalapp.utils.IdGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock

class JournalEditorViewModel(
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
                _journal.value = journalEntry
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    fun createNewJournalEntry(bookId: String, title: String = "") {
        viewModelScope.launch {
            try {
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    val newJournal = JournalEntry(
                        id = IdGenerator.generateId(),
                        ownerId = currentUser.id,
                        bookId = bookId,
                        title = title,
                        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                        blocks = emptyList()
                    )

                    journalRepository.createJournalEntry(newJournal)
                    _journal.value = newJournal
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun updateJournalTitle(title: String) {
        val currentJournal = _journal.value
        if (currentJournal != null) {
            val updatedJournal = currentJournal.copy(
                title = title,
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            _journal.value = updatedJournal
        }
    }
    
    fun updateBlock(index: Int, block: Block) {
        val currentJournal = _journal.value
        if (currentJournal != null) {
            val updatedBlocks = currentJournal.blocks.toMutableList()
            updatedBlocks[index] = block
            val updatedJournal = currentJournal.copy(
                blocks = updatedBlocks,
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            _journal.value = updatedJournal
        }
    }
    
    fun addBlock(block: Block) {
        val currentJournal = _journal.value
        if (currentJournal != null) {
            val updatedBlocks = currentJournal.blocks.toMutableList()
            updatedBlocks.add(block)
            val updatedJournal = currentJournal.copy(
                blocks = updatedBlocks,
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            _journal.value = updatedJournal
        }
    }
    
    fun deleteBlock(index: Int) {
        val currentJournal = _journal.value
        if (currentJournal != null && index >= 0 && index < currentJournal.blocks.size) {
            val updatedBlocks = currentJournal.blocks.toMutableList()
            updatedBlocks.removeAt(index)
            val updatedJournal = currentJournal.copy(
                blocks = updatedBlocks,
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            _journal.value = updatedJournal
        }
    }
    
    fun saveJournal() {
        viewModelScope.launch {
            try {
                val currentJournal = _journal.value
                if (currentJournal != null) {
                    // Save all blocks associated with this journal
                    currentJournal.blocks.forEach { block ->
                        journalRepository.createBlock(block)
                    }
                    
                    // Then save the journal entry itself
                    journalRepository.updateJournalEntry(currentJournal)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}