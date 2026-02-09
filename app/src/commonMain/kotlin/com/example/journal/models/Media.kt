package com.example.journal.models

import kotlinx.datetime.LocalDateTime

data class Media(
    val id: String,
    val ownerId: String,
    val type: MediaType,
    val localPath: String? = null,
    val remoteUrl: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val createdAt: LocalDateTime
)

enum class MediaType {
    IMAGE
}