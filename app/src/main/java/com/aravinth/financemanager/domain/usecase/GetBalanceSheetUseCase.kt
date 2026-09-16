package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.AccountCategory
import com.aravinth.financemanager.domain.model.AccountType
import com.aravinth.financemanager.domain.model.BalanceSheetItem
import com.aravinth.financemanager.domain.model.BalanceSheetReport
import com.aravinth.financemanager.domain.model.ChartOfAccount
import com.aravinth.financemanager.domain.repository.AccountingRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetBalanceSheetUseCase @Inject constructor(
    private val repository: AccountingRepo
) {
    operator fun invoke(startDate: Long? = null, endDate: Long? = null): Flow<BalanceSheetReport> {

        val transactionsFlow = if (startDate != null && endDate != null) {
            repository.viewTransactionByDateRange(startDate, endDate)
        } else {
            repository.viewAll()
        }

        return combine(transactionsFlow, repository.getAllAccounts()) { transactions, savedAccounts ->
            val debitTotals = mutableMapOf<String, Double>()
            val creditTotals = mutableMapOf<String, Double>()

            transactions.forEach { tx ->
                val drAcc = tx.debitAccount.ifEmpty { "Cash a/c" }
                val crAcc = tx.creditAccount.ifEmpty { "Bank a/c" }

                debitTotals[drAcc] = (debitTotals[drAcc] ?: 0.0) + tx.amount
                creditTotals[crAcc] = (creditTotals[crAcc] ?: 0.0) + tx.amount
            }

            val assets = mutableListOf<BalanceSheetItem>()
            val liabilities = mutableListOf<BalanceSheetItem>()
            val equities = mutableListOf<BalanceSheetItem>()

            var totalRevenue = 0.0
            var totalExpenses = 0.0
            var totalDrawings = 0.0
            var totalDividends = 0.0

            // dynamically inject default accounts so they are mathematically processed
            val allAccounts = savedAccounts.toMutableList()
            if (allAccounts.none { it.accountName == "Cash a/c" }) {
                allAccounts.add(ChartOfAccount(accountName = "Cash a/c",
                    accountType = AccountType.ASSET,
                    accountCategory = AccountCategory.CURRENT_ASSET,
                    additionalInfo = ""))
            }
            if (allAccounts.none { it.accountName == "Bank a/c" }) {
                allAccounts.add(ChartOfAccount(accountName = "Bank a/c",
                    accountType = AccountType.ASSET,
                    accountCategory = AccountCategory.CURRENT_ASSET,
                    additionalInfo =  ""))
            }

            // Apply DEALER rules for normal balances:
            allAccounts.forEach { account ->
                val dr = debitTotals[account.accountName] ?: 0.0
                val cr = creditTotals[account.accountName] ?: 0.0

                when (account.accountType) {
                    AccountType.ASSET -> {
                        val balance = dr - cr
                        if (balance != 0.0) assets.add(BalanceSheetItem(account.accountName, balance))
                    }
                    AccountType.LIABILITY -> {
                        val balance = cr - dr
                        if (balance != 0.0) liabilities.add(BalanceSheetItem(account.accountName, balance))
                    }
                    AccountType.EQUITY -> {
                        val balance = cr - dr
                        if (balance != 0.0) equities.add(BalanceSheetItem(account.accountName, balance))
                    }
                    AccountType.REVENUE -> totalRevenue += (cr - dr)
                    AccountType.EXPENSE -> totalExpenses += (dr - cr)
                    AccountType.DRAWINGS -> totalDrawings += (dr - cr)
                    AccountType.DIVIDEND -> totalDividends += (dr - cr)
                }
            }

            // Roll temporary accounts into Equity:
            val netIncome = totalRevenue - totalExpenses
            if (netIncome != 0.0) {
                equities.add(BalanceSheetItem("Retained Earnings (Net Income)", netIncome))
            }
            if (totalDrawings != 0.0) {
                equities.add(BalanceSheetItem("Less: Drawings", -totalDrawings))
            }
            if (totalDividends != 0.0) {
                equities.add(BalanceSheetItem("Less: Dividends", -totalDividends))
            }

            val totalAssets = assets.sumOf { it.amount }
            val totalLiabilities = liabilities.sumOf { it.amount }
            val totalEquity = equities.sumOf { it.amount }

            BalanceSheetReport(
                assets = assets.filter { it.amount != 0.0 },
                liabilities = liabilities.filter { it.amount != 0.0 },
                equities = equities.filter { it.amount != 0.0 },
                totalAssets = totalAssets,
                totalLiabilities = totalLiabilities,
                totalLiabilitiesAndEquity = totalLiabilities + totalEquity
            )
        }
    }
}