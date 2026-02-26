package com.weave.app.database.composeApp

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.weave.app.database.WeaveDatabase
import com.weave.app.database.WeaveDatabaseQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<WeaveDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = WeaveDatabaseImpl.Schema

internal fun KClass<WeaveDatabase>.newInstance(driver: SqlDriver): WeaveDatabase =
    WeaveDatabaseImpl(driver)

private class WeaveDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), WeaveDatabase {
  override val weaveDatabaseQueries: WeaveDatabaseQueries = WeaveDatabaseQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE journals (
          |    id TEXT PRIMARY KEY,
          |    title TEXT NOT NULL,
          |    cover_style TEXT NOT NULL,
          |    created_at INTEGER NOT NULL,
          |    updated_at INTEGER NOT NULL,
          |    page_count INTEGER DEFAULT 0,
          |    thickness REAL DEFAULT 10.0
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE pages (
          |    id TEXT PRIMARY KEY,
          |    journal_id TEXT NOT NULL,
          |    date INTEGER NOT NULL,
          |    paper_style TEXT NOT NULL,
          |    background_color INTEGER DEFAULT 16777215, -- 0xFFFDFCF8 (WHITE)
          |    created_at INTEGER NOT NULL,
          |    updated_at INTEGER NOT NULL,
          |    FOREIGN KEY (journal_id) REFERENCES journals(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE page_elements (
          |    id TEXT PRIMARY KEY,
          |    page_id TEXT NOT NULL,
          |    type TEXT NOT NULL, -- 'STICKER', 'TAPE', 'PHOTO', 'TEXT'
          |    position_x REAL NOT NULL,
          |    position_y REAL NOT NULL,
          |    rotation REAL DEFAULT 0.0,
          |    scale REAL DEFAULT 1.0,
          |    z_index INTEGER DEFAULT 0,
          |    data TEXT NOT NULL, -- JSON 序列化的元素数据
          |    FOREIGN KEY (page_id) REFERENCES pages(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE materials (
          |    id TEXT PRIMARY KEY,
          |    name TEXT NOT NULL,
          |    category TEXT NOT NULL, -- 'STICKER', 'TAPE', 'PAPER', 'FRAME', 'PEN', 'STAMP'
          |    resource_path TEXT NOT NULL,
          |    rarity TEXT DEFAULT 'COMMON',
          |    is_unlocked INTEGER DEFAULT 0,
          |    unlock_condition TEXT,
          |    acquired_at INTEGER
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
