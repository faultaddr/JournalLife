package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.auth.AuthService
import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.Theme
import com.pyy.journalapp.models.Visibility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val journalRepository: JournalRepository,
    private val authService: AuthService
) : ViewModel() {
    
    private val _currentTheme = MutableStateFlow(Theme.SYSTEM)
    val currentTheme: StateFlow<Theme> = _currentTheme
    
    private val _currentPrivacyDefault = MutableStateFlow(Visibility.PRIVATE)
    val currentPrivacyDefault: StateFlow<Visibility> = _currentPrivacyDefault
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    _currentTheme.value = currentUser.settings.theme
                    _currentPrivacyDefault.value = currentUser.settings.privacyDefaults
                }
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            try {
                _currentTheme.value = theme
                
                // Update user settings in database
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    // In a real app, we would update the user settings in the database
                    // For now, we'll just update the auth service state
                    val updatedUser = currentUser.copy(
                        settings = currentUser.settings.copy(theme = theme)
                    )
                    authService.updateCurrentUser(updatedUser)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun updatePrivacyDefault(privacyDefault: Visibility) {
        viewModelScope.launch {
            try {
                _currentPrivacyDefault.value = privacyDefault
                
                // Update user settings in database
                val currentUser = authService.currentUser.value
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        settings = currentUser.settings.copy(privacyDefaults = privacyDefault)
                    )
                    authService.updateCurrentUser(updatedUser)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authService.logout()
        }
    }
}

