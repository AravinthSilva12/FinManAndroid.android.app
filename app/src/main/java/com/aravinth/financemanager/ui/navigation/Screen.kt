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
    data object Budgeting

    @Serializable
    data object Assets : Screen
}
