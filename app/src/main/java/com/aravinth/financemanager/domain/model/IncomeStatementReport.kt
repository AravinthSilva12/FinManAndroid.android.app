package com.aravinth.financemanager.domain.model

data class IncomeStatementReport(
    val revenues: List<AccountSummary>,
    val cogs: List<AccountSummary>,   //Cost of goods sold
    val operatingExpenses: List<AccountSummary>,
    val interestAndTaxes: List<AccountSummary>,

    val totalRevenue: Double,
    val totalCogs: Double,
    val grossProfit: Double,     //Revenue - COGS

    val totalOperatingExpense: Double,
    val ebit: Double,  //Gross profit - Operating expenses

    val totalInterestAndTaxes: Double,
    val netProfit: Double  //EBIT - Interest & Taxes
)