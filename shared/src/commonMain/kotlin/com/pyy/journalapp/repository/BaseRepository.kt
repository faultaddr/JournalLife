package com.pyy.journalapp.repository

interface BaseRepository<T, ID> {
    suspend fun getAll(): List<T>
    suspend fun getById(id: ID): T?
    suspend fun insert(item: T): Long
    suspend fun update(item: T)
    suspend fun delete(id: ID)
}