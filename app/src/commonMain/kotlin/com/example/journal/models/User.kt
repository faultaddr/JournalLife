package com.example.journal.models

import kotlinx.datetime.LocalDateTime

data class User(
    val id: String,
    val email: String? = null,
    val phone: String? = null,
    val displayName: String,
    val avatarUrl: String? = null,
    val createdAt: LocalDateTime,
    val settings: UserSettings = UserSettings()
)

data class UserSettings(
    val locale: String = "en",
    val theme: Theme = Theme.SYSTEM,
    val privacyDefaults: Visibility = Visibility.PRIVATE
)

enum class Theme {
    LIGHT, DARK, SYSTEM
}

enum class Visibility {
    PUBLIC, PRIVATE
}