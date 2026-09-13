package com.aravinth.financemanager.domain.model

data class TrialBalanceReport(
        val activeAccounts: List<AccountSummary>,
        val totalDebit: Double,
        val totalCredit: Double,
        val isBalanced: Boolean
        )