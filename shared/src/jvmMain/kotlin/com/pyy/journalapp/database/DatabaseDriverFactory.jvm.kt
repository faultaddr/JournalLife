package com.pyy.journalapp.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pyy.journalapp.db.AppDatabase

/**
 * JVM 平台数据库驱动工厂实现
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // 使用本地 SQLite 数据库文件
        val driver = JdbcSqliteDriver("jdbc:sqlite:journal_life.db")
        // 创建表结构（如果不存在）
        AppDatabase.Schema.create(driver)
        return driver
    }
}
