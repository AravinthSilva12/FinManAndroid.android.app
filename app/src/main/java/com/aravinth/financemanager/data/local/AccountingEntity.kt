package com.aravinth.financemanager.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "accounting_table")
data class AccountingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val amount: Double,
    val category: String,
    val timestamp: Long,
    @ColumnInfo(defaultValue = "'Unknown'")
    val debitAccount: String,
    @ColumnInfo(defaultValue = "'Unknown'")
    val creditAccount: String,
    @ColumnInfo(defaultValue = "'Unknown'")
    val transactionType: String,
    @ColumnInfo(defaultValue = "''")
    val note: String = ""
)

@Entity(tableName = "cao_table", indices = [Index(value = ["accountName"], unique = true)])
data class ChartOfAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val accountName: String
)
