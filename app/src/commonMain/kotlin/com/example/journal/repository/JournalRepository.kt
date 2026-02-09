package com.example.journal.repository

import com.example.journal.models.*

interface JournalRepository {
    // User operations
    suspend fun createUser(user: User): Result<User>
    suspend fun getUser(userId: String): Result<User?>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(userId: String): Result<Unit>
    
    // Book operations
    suspend fun createBook(book: Book): Result<Book>
    suspend fun getBook(bookId: String): Result<Book?>
    suspend fun getBooksByOwner(ownerId: String): Result<List<Book>>
    suspend fun updateBook(book: Book): Result<Book>
    suspend fun deleteBook(bookId: String): Result<Unit>
    
    // Journal operations
    suspend fun createJournal(journal: JournalEntry): Result<JournalEntry>
    suspend fun getJournal(journalId: String): Result<JournalEntry?>
    suspend fun getJournalsByBook(bookId: String): Result<List<JournalEntry>>
    suspend fun getJournalsByOwner(ownerId: String): Result<List<JournalEntry>>
    suspend fun updateJournal(journal: JournalEntry): Result<JournalEntry>
    suspend fun deleteJournal(journalId: String): Result<Unit>
    
    // Block operations
    suspend fun addBlockToJournal(journalId: String, block: Block): Result<JournalEntry>
    suspend fun updateBlockInJournal(journalId: String, block: Block): Result<JournalEntry>
    suspend fun removeBlockFromJournal(journalId: String, blockId: String): Result<JournalEntry>
    
    // Share operations
    suspend fun createShare(share: Share): Result<Share>
    suspend fun getShareByToken(token: String): Result<Share?>
    suspend fun deleteShare(shareId: String): Result<Unit>
    
    // Statistics
    suspend fun getWritingFrequency(ownerId: String, startDate: kotlinx.datetime.LocalDateTime, endDate: kotlinx.datetime.LocalDateTime): Result<Map<kotlinx.datetime.LocalDate, Int>>
    suspend fun getWordCountStats(ownerId: String, startDate: kotlinx.datetime.LocalDateTime, endDate: kotlinx.datetime.LocalDateTime): Result<Int>
    suspend fun getImageCountStats(ownerId: String, startDate: kotlinx.datetime.LocalDateTime, endDate: kotlinx.datetime.LocalDateTime): Result<Int>
    suspend fun getTagDistribution(ownerId: String, startDate: kotlinx.datetime.LocalDateTime, endDate: kotlinx.datetime.LocalDateTime): Result<Map<String, Int>>
}