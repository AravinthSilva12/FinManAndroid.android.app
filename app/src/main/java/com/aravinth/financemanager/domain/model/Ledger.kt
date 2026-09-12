package com.aravinth.financemanager.domain.model

data class AccountSummary(
    val accountName: String,
    val totalDebit: Double,
    val totalCredit: Double,
    val netBalance: Double

)

data class TAccountDetail(
    val accountName: String,
    val openingBalanceBD: Double,
    val closingBalanceCD: Double,
    val debitEntries: List<Accounting>,
    val creditEntries: List<Accounting>,
    val totalDebitSide: Double,
    val totalCreditSide: Double,
    val isDebitBalance: Boolean,
    val isCreditBalance: Boolean
)