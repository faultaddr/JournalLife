package com.pyy.journalapp.database

import com.pyy.journalapp.db.AppDatabase
import app.cash.sqldelight.db.SqlDriver

class JournalDatabase(driver: SqlDriver) {
    val db = AppDatabase(
        driver = driver
    )

    val usersQueries get() = db.appDatabaseQueries
    val booksQueries get() = db.appDatabaseQueries
    val journalEntriesQueries get() = db.appDatabaseQueries
    val blocksQueries get() = db.appDatabaseQueries
    val mediaQueries get() = db.appDatabaseQueries
    val sharesQueries get() = db.appDatabaseQueries

    companion object {
        fun create(driver: SqlDriver): JournalDatabase = JournalDatabase(driver)
    }
}

// Block entity for database mapping (since Block is a sealed class)
data class BlockEntity(
    val id: String,
    val entry_id: String,
    val type: String,
    val created_at: Long,
    val updated_at: Long,
    val order_index: Long,  // Changed to Long to match generated code
    val text_content: String?,
    val image_id: String?,
    val caption: String?,
    val layout: String?,
    val crop_data: String?,
    val style_data: String?,
    val format: String?,
    val completed: Long?,  // Changed to Long to match generated code
    val heading_level: Long?  // Changed to Long to match generated code
)