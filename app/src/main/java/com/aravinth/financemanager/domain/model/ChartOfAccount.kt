package com.aravinth.financemanager.domain.model

data class ChartOfAccount (
    val id: Long = 0L,
    val accountName: String,
    val accountType: AccountType,
    val accountCategory: AccountCategory,
    val additionalInfo: String
)