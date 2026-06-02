package com.aravinth.financemanager.domain.model

data class Accounting(
    val id: Int = 0,
    val amount: Double,
    val category: Enum<TransactionCategory>,
    val debitAccount: String,
    val creditAccount: String,
    val type: Enum<TransactionType>,
    val timestamp: Long
)