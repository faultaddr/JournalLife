package com.pyy.journalapp.database

import app.cash.sqldelight.db.SqlDriver

/**
 * 平台特定的数据库驱动工厂
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

/**
 * 创建数据库实例
 */
fun createDatabase(driverFactory: DatabaseDriverFactory): JournalDatabase {
    val driver = driverFactory.createDriver()
    return JournalDatabase(driver)
}
