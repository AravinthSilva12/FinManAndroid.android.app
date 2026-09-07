package com.aravinth.financemanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [AccountingEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountingDao(): AccountingDao
}
