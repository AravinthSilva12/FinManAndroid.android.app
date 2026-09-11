package com.aravinth.financemanager.domain.model

data class Accounting(
    val id: Long = 0L,
    val amount: Double,
    val category: Enum<TransactionCategory>,
    val debitAccount: String,
    val creditAccount: String,
    val transactionType: Enum<TransactionType>,
    val timestamp: Long,
    val note: String = ""
)