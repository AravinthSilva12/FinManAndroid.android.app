package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.TAccountDetail
import com.aravinth.financemanager.domain.repository.AccountingRepo
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.abs

class GetTAccountUseCase @Inject constructor(
    private val transactionRepo: AccountingRepo
) {
    operator fun invoke(accountName: String): Flow<TAccountDetail> {
        return transactionRepo.viewAll().map { transactions ->
            //separate the entries:
            val debitEntries = transactions.filter { it.debitAccount.ifEmpty {"Cash a/c"} == accountName}
            val creditEntries = transactions.filter {it.creditAccount.ifEmpty { "Bank a/c"} == accountName}

            //calculate raw totals:
            val rawTotalDebit = debitEntries.sumOf { it.amount }
            val rawTotalCredit = creditEntries.sumOf { it.amount }

            //True T-account balancing logic:
            val balancedTotal = maxOf(rawTotalDebit, rawTotalCredit)

            TAccountDetail(
                accountName = accountName,
                openingBalanceBD = 0.0,
                closingBalanceCD = abs(rawTotalDebit - rawTotalCredit),
                debitEntries = debitEntries.sortedBy { it.timestamp },
                creditEntries = creditEntries.sortedBy { it.timestamp },
                //These will always equal each other in a balanced T-Account:
                totalDebitSide = balancedTotal,
                totalCreditSide = balancedTotal,
                //Helper flags for the UI to know where to draw the c/d line:
                isDebitBalance = rawTotalDebit > rawTotalCredit,
                isCreditBalance = rawTotalCredit > rawTotalDebit
            )
        }
    }
}