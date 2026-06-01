package com.aravinth.financemanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounting_table")
data class AccountingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val debitAccount: String,
    val creditAccount: String,
    val type: String,
    val timestamp: Long
)
