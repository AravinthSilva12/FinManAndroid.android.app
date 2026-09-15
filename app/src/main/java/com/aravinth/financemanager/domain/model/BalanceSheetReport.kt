package com.aravinth.financemanager.domain.model

data class BalanceSheetItem(
    val accountName: String,
    val amount: Double
)

data class BalanceSheetReport(
    val assets: List<BalanceSheetItem>,
    val liabilities: List<BalanceSheetItem>,
    val equities: List<BalanceSheetItem>,
    val totalAssets: Double,
    val totalLiabilities: Double,
    val totalLiabilitiesAndEquity: Double
)