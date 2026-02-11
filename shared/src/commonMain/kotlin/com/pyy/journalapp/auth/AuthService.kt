package com.pyy.journalapp.auth

import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.User
import com.pyy.journalapp.models.UserSettings
import com.pyy.journalapp.utils.IdGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class AuthService(private val journalRepository: JournalRepository) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _authState = MutableStateFlow(AuthState.NOT_AUTHENTICATED)
    val authState: StateFlow<AuthState> = _authState

    @OptIn(ExperimentalTime::class)
    suspend fun register(email: String, password: String, displayName: String): Result<User> {
        return try {
            // In a real app, you would hash the password and store it securely
            // For MVP, we'll just create a user without password storage
            val userId = IdGenerator.generateId()
            val newUser = User(
                id = userId,
                email = email,
                displayName = displayName,
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                settings = UserSettings()
            )

            journalRepository.createUser(newUser)
            _currentUser.value = newUser
            _authState.value = AuthState.AUTHENTICATED

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // In a real app, you would verify the password
            // For MVP, we'll just find the user by email
            val users = journalRepository.getUsersByEmail(email)
            val user = users.firstOrNull()

            if (user != null) {
                _currentUser.value = user
                _authState.value = AuthState.AUTHENTICATED
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.NOT_AUTHENTICATED
    }

    fun isAuthenticated(): Boolean {
        return _authState.value == AuthState.AUTHENTICATED
    }

    fun updateCurrentUser(user: User) {
        _currentUser.value = user
    }
}

enum class AuthState {
    AUTHENTICATED, NOT_AUTHENTICATED
}