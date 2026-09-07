package com.aravinth.financemanager.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounting_table")
data class AccountingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val timestamp: Long,
    @ColumnInfo(defaultValue = "'Unknown'")
    val debitAccount: String,
    @ColumnInfo(defaultValue = "'Unknown'")
    val creditAccount: String,
    @ColumnInfo(defaultValue = "'Unknown'")
    val transactionType: String
)
