package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.IncomeStatementReport
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.abs

class GetIncomeStatementUseCase @Inject constructor(
    private val getLedgerAccountsUseCase: GetLedgerAccountsUseCase)
{
    private val revenueKeywords = listOf("Sales", "Consulting", "Service")
    private val cogsKeywords = listOf("Purchases", "Raw Materials", "Direct Labor" )
    private val operatingKeywords = listOf("Software", "Rent", "Salary", "Marketing", "Furniture")
    private val interestTaxKeywords = listOf("Interest", "Tax", "Bank Charges")

    operator fun invoke(): Flow<IncomeStatementReport> {
       return getLedgerAccountsUseCase().map {ledgerAccounts ->
           //Categorize accounts:
           val revenues = ledgerAccounts.filter { acc ->
               revenueKeywords.any { acc.accountName.contains(it, true)}}
           val cogs = ledgerAccounts.filter { acc -> cogsKeywords.any { acc.accountName.contains(it, true)}}
           val operatingExpenses = ledgerAccounts.filter{ acc -> operatingKeywords.any {acc.accountName.contains(it, true)}}
           val interestAndTaxes = ledgerAccounts.filter{ acc-> interestTaxKeywords.any { acc.accountName.contains(it, true)}}

           //Tally the categories:
           //Revenues are credits (negative netBalance), so we use abs():
           val totalRevenue = revenues.sumOf {abs(it.netBalance)}

           //Expenses/COGS are Debits (positive netBalance):
           val totalCogs = cogs.sumOf{ it.netBalance}
           val totalOperatingExpense = operatingExpenses.sumOf {it.netBalance}
           val totalInterestAndTaxes = interestAndTaxes.sumOf{it.netBalance}

           //Calculate cascading Margins:
           val grossProfit = totalRevenue - totalCogs
           val ebit = grossProfit - totalOperatingExpense
           val netProfit = ebit - totalInterestAndTaxes

           IncomeStatementReport(
               revenues = revenues,
               cogs = cogs,
               operatingExpenses = operatingExpenses,
               interestAndTaxes = interestAndTaxes,
               totalRevenue = totalRevenue,
               totalCogs = totalCogs,
               grossProfit = grossProfit,
               totalOperatingExpense = totalOperatingExpense,
               ebit = ebit,
               totalInterestAndTaxes = totalInterestAndTaxes,
               netProfit = netProfit
           )
           }
       }
    }