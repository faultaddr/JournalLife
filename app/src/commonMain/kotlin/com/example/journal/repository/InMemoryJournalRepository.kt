package com.example.journal.repository

import com.example.journal.models.*
import kotlinx.coroutines.delay
import kotlinx.datetime.*

class InMemoryJournalRepository : JournalRepository {
    private val users = mutableMapOf<String, User>()
    private val books = mutableMapOf<String, Book>()
    private val journals = mutableMapOf<String, JournalEntry>()
    private val shares = mutableMapOf<String, Share>()
    
    override suspend fun createUser(user: User): Result<User> {
        delay(100) // Simulate network delay
        users[user.id] = user
        return Result.success(user)
    }
    
    override suspend fun getUser(userId: String): Result<User?> {
        delay(100)
        return Result.success(users[userId])
    }
    
    override suspend fun updateUser(user: User): Result<User> {
        delay(100)
        users[user.id] = user
        return Result.success(user)
    }
    
    override suspend fun deleteUser(userId: String): Result<Unit> {
        delay(100)
        users.remove(userId)
        return Result.success(Unit)
    }
    
    override suspend fun createBook(book: Book): Result<Book> {
        delay(100)
        books[book.id] = book
        return Result.success(book)
    }
    
    override suspend fun getBook(bookId: String): Result<Book?> {
        delay(100)
        return Result.success(books[bookId])
    }
    
    override suspend fun getBooksByOwner(ownerId: String): Result<List<Book>> {
        delay(100)
        return Result.success(books.values.filter { it.ownerId == ownerId })
    }
    
    override suspend fun updateBook(book: Book): Result<Book> {
        delay(100)
        books[book.id] = book
        return Result.success(book)
    }
    
    override suspend fun deleteBook(bookId: String): Result<Unit> {
        delay(100)
        books.remove(bookId)
        // Also delete associated journals
        journals.entries.removeAll { it.value.bookId == bookId }
        return Result.success(Unit)
    }
    
    override suspend fun createJournal(journal: JournalEntry): Result<JournalEntry> {
        delay(100)
        journals[journal.id] = journal
        return Result.success(journal)
    }
    
    override suspend fun getJournal(journalId: String): Result<JournalEntry?> {
        delay(100)
        return Result.success(journals[journalId])
    }
    
    override suspend fun getJournalsByBook(bookId: String): Result<List<JournalEntry>> {
        delay(100)
        return Result.success(journals.values.filter { it.bookId == bookId })
    }
    
    override suspend fun getJournalsByOwner(ownerId: String): Result<List<JournalEntry>> {
        delay(100)
        return Result.success(journals.values.filter { it.ownerId == ownerId })
    }
    
    override suspend fun updateJournal(journal: JournalEntry): Result<JournalEntry> {
        delay(100)
        journals[journal.id] = journal
        return Result.success(journal)
    }
    
    override suspend fun deleteJournal(journalId: String): Result<Unit> {
        delay(100)
        journals.remove(journalId)
        return Result.success(Unit)
    }
    
    override suspend fun addBlockToJournal(journalId: String, block: Block): Result<JournalEntry> {
        delay(100)
        val journal = journals[journalId] ?: return Result.failure(Exception("Journal not found"))
        
        // Create a new journal with the added block
        val updatedBlocks = journal.blocks.toMutableList()
        updatedBlocks.add(block)
        updatedBlocks.sortBy { it.orderIndex }
        
        val updatedJournal = journal.copy(
            blocks = updatedBlocks,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            metricsCache = calculateMetrics(updatedBlocks)
        )
        
        journals[journalId] = updatedJournal
        return Result.success(updatedJournal)
    }
    
    override suspend fun updateBlockInJournal(journalId: String, block: Block): Result<JournalEntry> {
        delay(100)
        val journal = journals[journalId] ?: return Result.failure(Exception("Journal not found"))
        
        // Create a new journal with the updated block
        val updatedBlocks = journal.blocks.map { 
            if (it.id == block.id) block else it 
        }
        
        val updatedJournal = journal.copy(
            blocks = updatedBlocks,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            metricsCache = calculateMetrics(updatedBlocks)
        )
        
        journals[journalId] = updatedJournal
        return Result.success(updatedJournal)
    }
    
    override suspend fun removeBlockFromJournal(journalId: String, blockId: String): Result<JournalEntry> {
        delay(100)
        val journal = journals[journalId] ?: return Result.failure(Exception("Journal not found"))
        
        // Create a new journal without the specified block
        val updatedBlocks = journal.blocks.filter { it.id != blockId }
        
        val updatedJournal = journal.copy(
            blocks = updatedBlocks,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            metricsCache = calculateMetrics(updatedBlocks)
        )
        
        journals[journalId] = updatedJournal
        return Result.success(updatedJournal)
    }
    
    override suspend fun createShare(share: Share): Result<Share> {
        delay(100)
        shares[share.id] = share
        return Result.success(share)
    }
    
    override suspend fun getShareByToken(token: String): Result<Share?> {
        delay(100)
        return Result.success(shares.values.find { it.shareToken == token })
    }
    
    override suspend fun deleteShare(shareId: String): Result<Unit> {
        delay(100)
        shares.remove(shareId)
        return Result.success(Unit)
    }
    
    override suspend fun getWritingFrequency(
        ownerId: String, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): Result<Map<LocalDate, Int>> {
        delay(100)
        val filteredJournals = journals.values
            .filter { it.ownerId == ownerId && 
                      it.createdAt.date >= startDate.date && 
                      it.createdAt.date <= endDate.date }
        
        val frequencyMap = mutableMapOf<LocalDate, Int>()
        filteredJournals.forEach { journal ->
            val date = journal.createdAt.date
            frequencyMap[date] = frequencyMap.getOrDefault(date, 0) + 1
        }
        
        return Result.success(frequencyMap)
    }
    
    override suspend fun getWordCountStats(
        ownerId: String, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): Result<Int> {
        delay(100)
        val totalWords = journals.values
            .filter { it.ownerId == ownerId && 
                      it.createdAt.date >= startDate.date && 
                      it.createdAt.date <= endDate.date }
            .sumOf { it.metricsCache.wordCount }
        
        return Result.success(totalWords)
    }
    
    override suspend fun getImageCountStats(
        ownerId: String, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): Result<Int> {
        delay(100)
        val totalImages = journals.values
            .filter { it.ownerId == ownerId && 
                      it.createdAt.date >= startDate.date && 
                      it.createdAt.date <= endDate.date }
            .sumOf { it.metricsCache.imageCount }
        
        return Result.success(totalImages)
    }
    
    override suspend fun getTagDistribution(
        ownerId: String, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): Result<Map<String, Int>> {
        delay(100)
        val tagCounts = mutableMapOf<String, Int>()
        
        journals.values
            .filter { it.ownerId == ownerId && 
                      it.createdAt.date >= startDate.date && 
                      it.createdAt.date <= endDate.date }
            .forEach { journal ->
                journal.tags.forEach { tag ->
                    tagCounts[tag] = tagCounts.getOrDefault(tag, 0) + 1
                }
            }
        
        return Result.success(tagCounts)
    }
    
    private fun calculateMetrics(blocks: List<Block>): MetricsCache {
        var wordCount = 0
        var imageCount = 0
        
        blocks.forEach { block ->
            when (block) {
                is TextBlock -> {
                    wordCount += block.text.split("\\s+".toRegex()).size
                }
                is ImageBlock -> {
                    imageCount++
                }
                else -> {}
            }
        }
        
        return MetricsCache(wordCount, imageCount)
    }
}