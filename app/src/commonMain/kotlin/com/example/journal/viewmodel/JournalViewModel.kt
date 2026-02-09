package com.example.journal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journal.models.*
import com.example.journal.repository.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JournalViewModel(private val repository: JournalRepository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _journals = MutableStateFlow<List<JournalEntry>>(emptyList())
    val journals: StateFlow<List<JournalEntry>> = _journals

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook

    private val _selectedJournal = MutableStateFlow<JournalEntry?>(null)
    val selectedJournal: StateFlow<JournalEntry?> = _selectedJournal

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            // In a real app, we would get the current user from auth system
            _currentUser.value = User(
                id = "user1",
                email = "user@example.com",
                displayName = "John Doe",
                createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC)
            )
            loadBooks()
            _isLoading.value = false
        }
    }

    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            currentUser.value?.id?.let { userId ->
                repository.getBooksByOwner(userId)
                    .onSuccess { books ->
                        _books.value = books
                    }
                    .onFailure { exception ->
                        _error.value = exception.message
                    }
            }
            _isLoading.value = false
        }
    }

    fun loadJournalsForBook(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getJournalsByBook(bookId)
                .onSuccess { journals ->
                    _journals.value = journals
                    // Find and set the selected book
                    _selectedBook.value = _books.value.find { it.id == bookId }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun loadJournalsForCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            currentUser.value?.id?.let { userId ->
                repository.getJournalsByOwner(userId)
                    .onSuccess { journals ->
                        _journals.value = journals
                    }
                    .onFailure { exception ->
                        _error.value = exception.message
                    }
            }
            _isLoading.value = false
        }
    }

    fun loadJournal(journalId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getJournal(journalId)
                .onSuccess { journal ->
                    _selectedJournal.value = journal
                    journal?.bookId?.let { bookId ->
                        repository.getBook(bookId)
                            .onSuccess { book ->
                                _selectedBook.value = book
                            }
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun createBook(title: String, description: String? = null, visibility: Visibility = Visibility.PRIVATE) {
        viewModelScope.launch {
            _isLoading.value = true
            val newBook = Book(
                id = "book_${System.currentTimeMillis()}",
                ownerId = currentUser.value?.id ?: return@launch,
                title = title,
                description = description,
                visibilityDefault = visibility,
                createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC)
            )
            
            repository.createBook(newBook)
                .onSuccess { book ->
                    _books.value = _books.value + book
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun createJournal(
        bookId: String, 
        title: String, 
        visibility: Visibility = Visibility.PRIVATE,
        blocks: List<Block> = emptyList()
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val newJournal = JournalEntry(
                id = "journal_${System.currentTimeMillis()}",
                ownerId = currentUser.value?.id ?: return@launch,
                bookId = bookId,
                title = title,
                createdAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                updatedAt = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
                visibility = visibility,
                blocks = blocks
            )
            
            repository.createJournal(newJournal)
                .onSuccess { journal ->
                    _journals.value = _journals.value + journal
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun updateJournal(journal: JournalEntry) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateJournal(journal)
                .onSuccess { updatedJournal ->
                    _journals.value = _journals.value.map { 
                        if (it.id == updatedJournal.id) updatedJournal else it 
                    }
                    _selectedJournal.value = updatedJournal
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun deleteJournal(journalId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteJournal(journalId)
                .onSuccess {
                    _journals.value = _journals.value.filter { it.id != journalId }
                    if (_selectedJournal.value?.id == journalId) {
                        _selectedJournal.value = null
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun addBlockToJournal(journalId: String, block: Block) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.addBlockToJournal(journalId, block)
                .onSuccess { updatedJournal ->
                    _journals.value = _journals.value.map { 
                        if (it.id == journalId) updatedJournal else it 
                    }
                    if (_selectedJournal.value?.id == journalId) {
                        _selectedJournal.value = updatedJournal
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun updateBlockInJournal(journalId: String, block: Block) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateBlockInJournal(journalId, block)
                .onSuccess { updatedJournal ->
                    _journals.value = _journals.value.map { 
                        if (it.id == journalId) updatedJournal else it 
                    }
                    if (_selectedJournal.value?.id == journalId) {
                        _selectedJournal.value = updatedJournal
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun removeBlockFromJournal(journalId: String, blockId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.removeBlockFromJournal(journalId, blockId)
                .onSuccess { updatedJournal ->
                    _journals.value = _journals.value.map { 
                        if (it.id == journalId) updatedJournal else it 
                    }
                    if (_selectedJournal.value?.id == journalId) {
                        _selectedJournal.value = updatedJournal
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun setError(error: String?) {
        _error.value = error
    }
}