package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.data.repository.RoomAccountingRepository
import com.aravinth.financemanager.domain.model.AccountSummary
import com.aravinth.financemanager.domain.repository.AccountingRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetLedgerAccountsUseCase @Inject constructor(
    val transactionRepo: AccountingRepo,
    val accountRepo: RoomAccountingRepository
) {
    operator fun invoke(): Flow<List<AccountSummary>> {
        val transactionsFlow = transactionRepo.viewAll()
        val accountsFlow = accountRepo.getAllAccounts()
        return combine(transactionsFlow, accountsFlow) {transactions, savedAccounts ->
            val allAccounts = (listOf("Cash a/c, Bank a/c") + savedAccounts).distinct()

            allAccounts.map { accountName ->
                val totalDebit = transactions.filter{it.debitAccount.ifEmpty{"Cash a/c"} == accountName}
                    .sumOf{it.amount}

                val totalCredit = transactions.filter{it.creditAccount.ifEmpty{"Bank a/c"} == accountName}
                    .sumOf{it.amount}

                AccountSummary(
                    accountName = accountName,
                    totalDebit = totalDebit,
                    totalCredit = totalCredit,
                    netBalance = totalDebit - totalCredit
                )
            }.sortedBy{ it.accountName }
        }
    }
}