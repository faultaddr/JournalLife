package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.auth.AuthService
import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.Book
import com.pyy.journalapp.utils.DateTimeUtils
import com.pyy.journalapp.utils.IdGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock

class HomeViewModel(
    private val journalRepository: JournalRepository,
    private val authService: AuthService
) : ViewModel() {
    
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadBooks()
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    val books = journalRepository.getBooksByOwnerId(currentUser.id)
                    _books.value = books
                }
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    fun addBook(title: String, description: String? = null) {
        viewModelScope.launch {
            try {
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    val newBook = Book(
                        id = IdGenerator.generateId(),
                        ownerId = currentUser.id,
                        title = title,
                        description = description,
                        createdAt = DateTimeUtils.now(),
                        updatedAt = DateTimeUtils.now()
                    )

                    journalRepository.createBook(newBook)
                    loadBooks() // Refresh the list
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}