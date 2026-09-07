package com.aravinth.financemanager.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2){
    override fun migrate(db: SupportSQLiteDatabase){
        db.execSQL("ALTER TABLE accounting_table ADD COLUMN debitAccount TEXT NOT NULL DEFAULT 'Unknown'")
        db.execSQL("ALTER TABLE accounting_table ADD COLUMN creditAccount TEXT NOT NULL DEFAULT 'Unknown'")
        db.execSQL("ALTER TABLE accounting_table ADD COLUMN type TEXT NOT NULL DEFAULT 'Unknown'")
        db.execSQL("ALTER TABLE accounting_table ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0")
    }
}