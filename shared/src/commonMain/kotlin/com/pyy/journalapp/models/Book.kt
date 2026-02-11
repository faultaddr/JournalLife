package com.pyy.journalapp.models

import kotlinx.datetime.LocalDateTime

data class Book(
    val id: String,
    val ownerId: String,
    val title: String,
    val coverImageId: String? = null,
    val description: String? = null,
    val visibilityDefault: Visibility = Visibility.PRIVATE,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)