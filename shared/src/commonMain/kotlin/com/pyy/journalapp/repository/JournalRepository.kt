package com.pyy.journalapp.repository

import com.pyy.journalapp.database.BlockEntity
import com.pyy.journalapp.database.JournalDatabase
import com.pyy.journalapp.models.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class JournalRepository(private val database: JournalDatabase) {
    
    // User operations
    @OptIn(ExperimentalTime::class)
    suspend fun createUser(user: User) {
        database.db.appDatabaseQueries.users_insert(
            id = user.id,
            email = user.email,
            phone = user.phone,
            display_name = user.displayName,
            avatar_url = user.avatarUrl,
            created_at = user.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            locale = user.settings.locale,
            theme = user.settings.theme.name,
            privacy_defaults = user.settings.privacyDefaults.name
        )
    }

    suspend fun getUserById(id: String): User? {
        val userEntity = database.db.appDatabaseQueries.users_selectById(id)
            .executeAsOneOrNull()
        return userEntity?.toUserModel()
    }

    suspend fun getUsersByEmail(email: String): List<User> {
        return database.db.appDatabaseQueries.users_selectByEmail(email)
            .executeAsList()
            .map { it.toUserModel() }
    }

    // Book operations
    @OptIn(ExperimentalTime::class)
    suspend fun createBook(book: Book) {
        database.db.appDatabaseQueries.books_insert(
            id = book.id,
            owner_id = book.ownerId,
            title = book.title,
            cover_image_id = book.coverImageId,
            description = book.description,
            visibility_default = book.visibilityDefault.name,
            created_at = book.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            updated_at = book.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
    }

    suspend fun getBooksByOwnerId(ownerId: String): List<Book> {
        return database.db.appDatabaseQueries.books_selectByOwner(ownerId)
            .executeAsList()
            .map { it.toBookModel() }
    }

    suspend fun getBookById(id: String): Book? {
        val bookEntity = database.db.appDatabaseQueries.books_selectById(id)
            .executeAsOneOrNull()
        return bookEntity?.toBookModel()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun updateBook(book: Book) {
        database.db.appDatabaseQueries.books_update(
            owner_id = book.ownerId,
            title = book.title,
            cover_image_id = book.coverImageId,
            description = book.description,
            visibility_default = book.visibilityDefault.name,
            updated_at = book.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            id = book.id
        )
    }

    suspend fun deleteBook(id: String) {
        database.db.appDatabaseQueries.books_deleteById(id)
    }

    // Journal Entry operations
    @OptIn(ExperimentalTime::class)
    suspend fun createJournalEntry(journalEntry: JournalEntry) {
        database.db.appDatabaseQueries.journal_entries_insert(
            id = journalEntry.id,
            owner_id = journalEntry.ownerId,
            book_id = journalEntry.bookId,
            title = journalEntry.title,
            created_at = journalEntry.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            updated_at = journalEntry.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            visibility = journalEntry.visibility.name,
            tags = journalEntry.tags.joinToString(","), // Convert list to string for storage
            word_count = journalEntry.metricsCache.wordCount.toLong(),
            image_count = journalEntry.metricsCache.imageCount.toLong()
        )
    }

    suspend fun getJournalEntriesByBookId(bookId: String): List<JournalEntry> {
        return database.db.appDatabaseQueries.journal_entries_selectByBook(bookId)
            .executeAsList()
            .map { it.toJournalEntryModel() }
    }

    suspend fun getJournalEntryById(id: String): JournalEntry? {
        val journalEntity = database.db.appDatabaseQueries.journal_entries_selectById(id)
            .executeAsOneOrNull()
        return journalEntity?.toJournalEntryModel()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun updateJournalEntry(journalEntry: JournalEntry) {
        database.db.appDatabaseQueries.journal_entries_update(
            owner_id = journalEntry.ownerId,
            book_id = journalEntry.bookId,
            title = journalEntry.title,
            updated_at = journalEntry.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            visibility = journalEntry.visibility.name,
            tags = journalEntry.tags.joinToString(","), // Convert list to string for storage
            word_count = journalEntry.metricsCache.wordCount.toLong(),
            image_count = journalEntry.metricsCache.imageCount.toLong(),
            id = journalEntry.id
        )
    }

    suspend fun deleteJournalEntry(id: String) {
        database.db.appDatabaseQueries.journal_entries_deleteById(id)
    }

    // Block operations
    suspend fun createBlock(block: Block) {
        val blockEntity = block.toBlockEntity()
        database.db.appDatabaseQueries.blocks_insert(
            id = blockEntity.id,
            entry_id = blockEntity.entry_id,
            type = blockEntity.type,
            created_at = blockEntity.created_at,
            updated_at = blockEntity.updated_at,
            order_index = blockEntity.order_index,
            text_content = blockEntity.text_content,
            image_id = blockEntity.image_id,
            caption = blockEntity.caption,
            layout = blockEntity.layout,
            crop_data = blockEntity.crop_data,
            style_data = blockEntity.style_data,
            format = blockEntity.format,
            completed = blockEntity.completed,
            heading_level = blockEntity.heading_level
        )
    }

    suspend fun getBlocksByEntryId(entryId: String): List<Block> {
        return database.db.appDatabaseQueries.blocks_selectByEntry(entryId)
            .executeAsList()
            .map { it.toBlockModel() }
    }

    suspend fun updateBlock(block: Block) {
        val blockEntity = block.toBlockEntity()
        database.db.appDatabaseQueries.blocks_update(
            entry_id = blockEntity.entry_id,
            type = blockEntity.type,
            updated_at = blockEntity.updated_at,
            order_index = blockEntity.order_index,
            text_content = blockEntity.text_content,
            image_id = blockEntity.image_id,
            caption = blockEntity.caption,
            layout = blockEntity.layout,
            crop_data = blockEntity.crop_data,
            style_data = blockEntity.style_data,
            format = blockEntity.format,
            completed = blockEntity.completed,
            heading_level = blockEntity.heading_level,
            id = blockEntity.id
        )
    }

    suspend fun deleteBlock(id: String) {
        database.db.appDatabaseQueries.blocks_deleteById(id)
    }

    // Media operations
    @OptIn(ExperimentalTime::class)
    suspend fun createMedia(media: Media) {
        database.db.appDatabaseQueries.media_insert(
            id = media.id,
            owner_id = media.ownerId,
            type = media.type.name, // Convert enum to string
            local_path = media.localPath,
            remote_url = media.remoteUrl,
            width = media.width?.toLong(),
            height = media.height?.toLong(),
            created_at = media.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
    }

    suspend fun getMediaByOwnerId(ownerId: String): List<Media> {
        return database.db.appDatabaseQueries.media_selectByOwner(ownerId)
            .executeAsList()
            .map { it.toMediaModel() }
    }

    suspend fun getMediaById(id: String): Media? {
        val mediaEntity = database.db.appDatabaseQueries.media_selectById(id)
            .executeAsOneOrNull()
        return mediaEntity?.toMediaModel()
    }

    suspend fun deleteMedia(id: String) {
        database.db.appDatabaseQueries.media_deleteById(id)
    }

    // Share operations
    @OptIn(ExperimentalTime::class)
    suspend fun createShare(share: Share) {
        database.db.appDatabaseQueries.shares_insert(
            id = share.id,
            target_type = share.targetType.name, // Convert enum to string
            target_id = share.targetId,
            owner_id = share.ownerId,
            visibility = share.visibility.name, // Convert enum to string
            share_token = share.shareToken,
            created_at = share.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            expired_at = share.expiredAt?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
        )
    }

    suspend fun getShareById(id: String): Share? {
        val shareEntity = database.db.appDatabaseQueries.shares_selectById(id)
            .executeAsOneOrNull()
        return shareEntity?.toShareModel()
    }

    suspend fun getSharesByTarget(targetId: String, targetType: ShareTargetType): List<Share> {
        return database.db.appDatabaseQueries.shares_selectByTarget(
            target_id = targetId,
            target_type = targetType.name
        ).executeAsList()
            .map { it.toShareModel() }
    }

    suspend fun deleteShare(id: String) {
        database.db.appDatabaseQueries.shares_deleteById(id)
    }
    
    // Helper extension functions to convert between entities and models
    @OptIn(ExperimentalTime::class)
    private fun com.pyy.journalapp.db.Users.toUserModel(): User {
        return User(
            id = this.id,
            email = this.email,
            phone = this.phone,
            displayName = this.display_name,
            avatarUrl = this.avatar_url,
            createdAt = Instant.fromEpochMilliseconds(this.created_at)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            settings = UserSettings(
                locale = this.locale ?: "en",
                theme = Theme.valueOf(this.theme ?: "SYSTEM"),
                privacyDefaults = Visibility.valueOf(this.privacy_defaults ?: "PRIVATE")
            )
        )
    }

    @OptIn(ExperimentalTime::class)
    private fun com.pyy.journalapp.db.Books.toBookModel(): Book {
        return Book(
            id = this.id,
            ownerId = this.owner_id,
            title = this.title,
            coverImageId = this.cover_image_id,
            description = this.description,
            visibilityDefault = Visibility.valueOf(this.visibility_default ?: "PRIVATE"),
            createdAt = Instant.fromEpochMilliseconds(this.created_at)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @OptIn(ExperimentalTime::class)
    private fun com.pyy.journalapp.db.Journal_entries.toJournalEntryModel(): JournalEntry {
        return JournalEntry(
            id = this.id,
            ownerId = this.owner_id,
            bookId = this.book_id,
            title = this.title,
            createdAt = Instant.fromEpochMilliseconds(this.created_at)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            visibility = Visibility.valueOf(this.visibility ?: "PRIVATE"),
            tags = if (this.tags != null) this.tags.split(",") else emptyList(),
            metricsCache = MetricsCache(
                wordCount = this.word_count?.toInt() ?: 0,
                imageCount = this.image_count?.toInt() ?: 0
            )
        )
    }

    @OptIn(ExperimentalTime::class)
    private fun Block.toBlockEntity(): BlockEntity {
        return when (this) {
            is TextBlock -> BlockEntity(
                id = this.id,
                entry_id = "", // Will be set when inserting
                type = this.type.name, // Convert enum to string
                created_at = this.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                updated_at = this.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                order_index = this.orderIndex.toLong(), // Convert Int to Long
                text_content = this.text,
                image_id = null,
                caption = null,
                layout = null,
                crop_data = null,
                style_data = null, // Would need to serialize style properly
                format = this.format.name, // Convert enum to string
                completed = null,
                heading_level = null
            )
            is ImageBlock -> BlockEntity(
                id = this.id,
                entry_id = "", // Will be set when inserting
                type = this.type.name, // Convert enum to string
                created_at = this.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                updated_at = this.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                order_index = this.orderIndex.toLong(), // Convert Int to Long
                text_content = null,
                image_id = this.imageId,
                caption = this.caption,
                layout = this.layout?.name, // Convert enum to string
                crop_data = null, // Would need to serialize crop properly
                style_data = null,
                format = null,
                completed = null,
                heading_level = null
            )
            is TodoBlock -> BlockEntity(
                id = this.id,
                entry_id = "", // Will be set when inserting
                type = this.type.name, // Convert enum to string
                created_at = this.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                updated_at = this.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                order_index = this.orderIndex.toLong(), // Convert Int to Long
                text_content = this.text,
                image_id = null,
                caption = null,
                layout = null,
                crop_data = null,
                style_data = null,
                format = null,
                completed = if (this.completed) 1L else 0L, // Convert Boolean to Long
                heading_level = null
            )
            is DividerBlock -> BlockEntity(
                id = this.id,
                entry_id = "", // Will be set when inserting
                type = this.type.name, // Convert enum to string
                created_at = this.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                updated_at = this.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                order_index = this.orderIndex.toLong(), // Convert Int to Long
                text_content = null,
                image_id = null,
                caption = null,
                layout = null,
                crop_data = null,
                style_data = null,
                format = null,
                completed = null,
                heading_level = null
            )
            is QuoteBlock -> BlockEntity(
                id = this.id,
                entry_id = "", // Will be set when inserting
                type = this.type.name, // Convert enum to string
                created_at = this.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                updated_at = this.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                order_index = this.orderIndex.toLong(), // Convert Int to Long
                text_content = this.text,
                image_id = null,
                caption = null,
                layout = null,
                crop_data = null,
                style_data = null,
                format = null,
                completed = null,
                heading_level = null
            )
            is HeadingBlock -> BlockEntity(
                id = this.id,
                entry_id = "", // Will be set when inserting
                type = this.type.name, // Convert enum to string
                created_at = this.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                updated_at = this.updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                order_index = this.orderIndex.toLong(), // Convert Int to Long
                text_content = this.text,
                image_id = null,
                caption = null,
                layout = null,
                crop_data = null,
                style_data = null,
                format = null,
                completed = null,
                heading_level = this.level.toLong() // Convert Int to Long
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun com.pyy.journalapp.db.Blocks.toBlockModel(): Block {
        return when (this.type) {
            "TEXT" -> TextBlock(
                id = this.id,
                createdAt = Instant.fromEpochMilliseconds(this.created_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                orderIndex = this.order_index.toInt(), // Convert Long to Int
                text = this.text_content ?: "",
                format = TextFormat.valueOf(this.format ?: "PLAIN")
            )
            "IMAGE" -> ImageBlock(
                id = this.id,
                createdAt = Instant.fromEpochMilliseconds(this.created_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                orderIndex = this.order_index.toInt(), // Convert Long to Int
                imageId = this.image_id ?: "",
                caption = this.caption,
                layout = if (this.layout != null) ImageLayout.valueOf(this.layout) else ImageLayout.INLINE
            )
            "TODO" -> TodoBlock(
                id = this.id,
                createdAt = Instant.fromEpochMilliseconds(this.created_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                orderIndex = this.order_index.toInt(), // Convert Long to Int
                text = this.text_content ?: "",
                completed = this.completed?.toInt() == 1 // Convert Long back to Boolean
            )
            "DIVIDER" -> DividerBlock(
                id = this.id,
                createdAt = Instant.fromEpochMilliseconds(this.created_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                orderIndex = this.order_index.toInt(), // Convert Long to Int
            )
            "QUOTE" -> QuoteBlock(
                id = this.id,
                createdAt = Instant.fromEpochMilliseconds(this.created_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                orderIndex = this.order_index.toInt(), // Convert Long to Int
                text = this.text_content ?: ""
            )
            "HEADING" -> HeadingBlock(
                id = this.id,
                createdAt = Instant.fromEpochMilliseconds(this.created_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Instant.fromEpochMilliseconds(this.updated_at)
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                orderIndex = this.order_index.toInt(), // Convert Long to Int
                text = this.text_content ?: "",
                level = this.heading_level?.toInt() ?: 1
            )
            else -> throw IllegalArgumentException("Unknown block type: ${this.type}")
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun com.pyy.journalapp.db.Media.toMediaModel(): Media {
        return Media(
            id = this.id,
            ownerId = this.owner_id,
            type = MediaType.valueOf(this.type),
            localPath = this.local_path,
            remoteUrl = this.remote_url,
            width = this.width?.toInt(),
            height = this.height?.toInt(),
            createdAt = Instant.fromEpochMilliseconds(this.created_at)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @OptIn(ExperimentalTime::class)
    private fun com.pyy.journalapp.db.Shares.toShareModel(): Share {
        return Share(
            id = this.id,
            targetType = ShareTargetType.valueOf(this.target_type),
            targetId = this.target_id,
            ownerId = this.owner_id,
            visibility = ShareVisibility.valueOf(this.visibility),
            shareToken = this.share_token,
            createdAt = Instant.fromEpochMilliseconds(this.created_at)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            expiredAt = if (this.expired_at != null) {
                Instant.fromEpochMilliseconds(this.expired_at!!)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            } else null
        )
    }
}