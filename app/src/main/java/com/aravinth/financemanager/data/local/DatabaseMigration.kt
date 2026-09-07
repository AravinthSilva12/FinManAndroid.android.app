package com.aravinth.financemanager.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2){
    override fun migrate(db: SupportSQLiteDatabase){
        // Recreate the table to remove the 'type' column and add new columns with correct schema
        db.execSQL("CREATE TABLE IF NOT EXISTS `accounting_table_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `amount` REAL NOT NULL, `category` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `debitAccount` TEXT NOT NULL DEFAULT 'Unknown', `creditAccount` TEXT NOT NULL DEFAULT 'Unknown', `transactionType` TEXT NOT NULL DEFAULT 'Unknown')")
        
        // Copy data from the old table to the new one
        // We map the old 'type' column to the new 'transactionType' column
        db.execSQL("INSERT INTO `accounting_table_new` (`id`, `amount`, `category`, `timestamp`, `debitAccount`, `creditAccount`, `transactionType`) SELECT `id`, `amount`, `category`, `timestamp`, 'Unknown', 'Unknown', `type` FROM `accounting_table` ")
        
        // Drop the old table and rename the new one
        db.execSQL("DROP TABLE `accounting_table` ")
        db.execSQL("ALTER TABLE `accounting_table_new` RENAME TO `accounting_table` ")
    }
}