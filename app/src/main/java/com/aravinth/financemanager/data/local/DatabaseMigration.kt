package com.aravinth.financemanager.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// VERSION 1 to 2: Restructuring the accounting table (without the note column yet)
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. Create the new table (Fixed the missing comma, removed 'note' to respect history)
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `accounting_table_new` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`amount` REAL NOT NULL, " +
                    "`category` TEXT NOT NULL, " +
                    "`timestamp` INTEGER NOT NULL, " +
                    "`debitAccount` TEXT NOT NULL DEFAULT 'Unknown', " +
                    "`creditAccount` TEXT NOT NULL DEFAULT 'Unknown', " +
                    "`transactionType` TEXT NOT NULL DEFAULT 'Unknown')"
        )

        // 2. Copy the data (Now inserting 7 columns, selecting 7 columns)
        db.execSQL(
            "INSERT INTO `accounting_table_new` (`id`, `amount`, `category`, `timestamp`, `debitAccount`, `creditAccount`, `transactionType`) " +
                    "SELECT `id`, `amount`, `category`, `timestamp`, 'Unknown', 'Unknown', `type` FROM `accounting_table`"
        )

        // 3. Drop old and rename new
        db.execSQL("DROP TABLE `accounting_table`")
        db.execSQL("ALTER TABLE `accounting_table_new` RENAME TO `accounting_table`")
    }
}

// VERSION 2 to 3: Adding the Chart of Accounts tables
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // (Verify if this should be coa_table instead of cao_table in your entity!)
        db.execSQL("CREATE TABLE IF NOT EXISTS `cao_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `accountName` TEXT NOT NULL)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_cao_table_accountName` ON `cao_table` (`accountName`)")
    }
}

// VERSION 3 to 4: Adding the note column to the accounting table
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `accounting_table` ADD COLUMN `note` TEXT NOT NULL DEFAULT ''")
    }
}

//VERSION 4 to 5 : Adding type, category, and info to the COA table
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        //changing table name:
        db.execSQL("ALTER TABLE `cao_table` RENAME TO `coa_table`")
        //deleting old indexes & adding new indexes:
        db.execSQL("DROP INDEX IF EXISTS `index_cao_table_accountName`")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_coa_table_accountName` ON `coa_table` (`accountName`)")
        //adding new columns:
        db.execSQL("ALTER TABLE `coa_table` ADD COLUMN `accountType` TEXT NOT NULL DEFAULT 'ASSET'")
        db.execSQL("ALTER TABLE `coa_table` ADD COLUMN `accountCategory` TEXT NOT NULL DEFAULT 'CURRENT_ASSET'")
        db.execSQL("ALTER TABLE `coa_table` ADD COLUMN `additionalInfo` TEXT NOT NULL DEFAULT ''")
    }
}