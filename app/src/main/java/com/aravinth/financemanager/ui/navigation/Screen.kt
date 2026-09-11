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
    data object Journal

    @Serializable
    data class EntryDetailScreen(val transactionId: Long) : Screen

    @Serializable
    data object Ledger

    @Serializable
    data object FinancialReport
    @Serializable
    data object Budgeting

    @Serializable
    data object Assets : Screen

}
