package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.IncomeStatementReport
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetIncomeStatementUseCase @Inject constructor(
    private val getLedgerAccountsUseCase: GetLedgerAccountsUseCase)
{
    private val revenueKeywords = listOf("Sales", "Consulting", "Service")
    private val cogsKeywords = listOf("Purchases", "Raw Materials", "Direct Labor" )
    private val operatingKeywords = listOf("Software", "Rent", "Salary", "Marketing", "Furniture")
    private val interestTaxesKeywords = listOf("Interest", "Tax", "Bank Charges")

    operator fun invoke(): Flow<IncomeStatementReport> {
       return getLedgerAccountsUseCase().map {ledgerAccounts ->

       }
    }



}