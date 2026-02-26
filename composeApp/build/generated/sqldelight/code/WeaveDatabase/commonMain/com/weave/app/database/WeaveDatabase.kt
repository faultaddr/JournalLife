package com.weave.app.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.weave.app.database.composeApp.newInstance
import com.weave.app.database.composeApp.schema
import kotlin.Unit

public interface WeaveDatabase : Transacter {
  public val weaveDatabaseQueries: WeaveDatabaseQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = WeaveDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): WeaveDatabase =
        WeaveDatabase::class.newInstance(driver)
  }
}
