package com.pyy.journalapp.models

import kotlinx.datetime.LocalDateTime

data class Share(
    val id: String,
    val targetType: ShareTargetType,
    val targetId: String,
    val ownerId: String,
    val visibility: ShareVisibility,
    val shareToken: String,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime? = null
)

enum class ShareTargetType {
    JOURNAL_ENTRY, BOOK
}

enum class ShareVisibility {
    PUBLIC_LINK, DISABLED
}