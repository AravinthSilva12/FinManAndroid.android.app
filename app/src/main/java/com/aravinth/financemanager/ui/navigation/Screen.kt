package com.aravinth.financemanager.ui.navigation

import kotlinx.serialization.Serializable

@Serializable sealed interface  Screen{

    @Serializable
    data object Main

    @Serializable
    data object Home : Screen

    @Serializable
    data object Accounting

    @Serializable
    data object AddTransaction

    @Serializable
    data class Journal(val targetTransactionId: Long = -1L) : Screen

    @Serializable
    data class EntryDetailScreen(val transactionId: Long = -1L) : Screen

    @Serializable
    data object Ledger

    @Serializable
    data object FinancialReport
    @Serializable
    data object Budgeting

    @Serializable
    data object Assets : Screen

}
