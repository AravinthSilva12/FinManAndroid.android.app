package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.AccountType
import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.domain.model.TransactionType
import com.aravinth.financemanager.domain.repository.AccountingRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CloseAccountingPeriodUseCase @Inject constructor(
    private val repository: AccountingRepo
) {
    suspend operator fun invoke(closingDateMillis: Long) {
        val transactions = repository.viewAll().first()
        val accounts = repository.getAllAccounts().first()

        val debitTotals = mutableMapOf<String, Double>()
        val creditTotals = mutableMapOf<String, Double>()

        //Tally up all debits and credits
        transactions.forEach {tx ->
            debitTotals[tx.debitAccount] = (debitTotals[tx.debitAccount] ?: 0.0) + tx.amount
            creditTotals[tx.creditAccount] = (creditTotals[tx.creditAccount] ?: 0.0) + tx.amount
        }

        val closingEntries = mutableListOf<Accounting>()
        val retainedEarnings = "Retained Earnings a/c"

        //Generate closing Journal entries for temporary accounts:
        accounts.forEach {account ->
            val dr = debitTotals[account.accountName] ?: 0.0
            val cr = creditTotals[account.accountName] ?: 0.0

            when (account.accountType) {
                AccountType.REVENUE -> {
                    val balance = cr - dr
                    if (balance > 0)
                        //Revenue has a credit balance. To zero it out, we DEBIT Revenue & CREDIT Retained Earnings:
                        closingEntries.add(createClosingEntry(amount = balance,
                            debitAcc = account.accountName,
                            creditAcc = retainedEarnings,
                            timestamp = closingDateMillis,
                            note = "Closing entry to zero out ${account.accountName}"))
                }
                AccountType.EXPENSE -> {
                    val balance = dr - cr
                    if (balance > 0)
                        //Expense has a Debit balance. To zero it out, we DEBIT Retained Earnings & CREDIT Expense:
                        closingEntries.add(createClosingEntry(amount = balance,
                            debitAcc = retainedEarnings,
                            creditAcc = account.accountName,
                            timestamp = closingDateMillis,
                            note = "Closing entry to zero out ${account.accountName}"
                        )
                    )
                }
                AccountType.DRAWINGS, AccountType.DIVIDEND -> {
                    val balance = dr - cr
                    if (balance > 0)
                        //Drawings have a Debit balance, we DEBIT Retained Earnings & CREDIT Drawings:
                        closingEntries.add(createClosingEntry(
                            amount = balance,
                            debitAcc = retainedEarnings,
                            creditAcc = account.accountName,
                            timestamp = closingDateMillis,
                            note = "Closing entry to zero out ${account.accountName}"))
                }
                else -> {}
            }
        }

        //Save all closing entries directly to the Room database:
        closingEntries.forEach {entry ->
            repository.addTransaction(entry)
        }
    }

    //Helper function to keep code clean:
    private fun createClosingEntry(
        amount: Double,
        debitAcc: String,
        creditAcc: String,
        timestamp: Long,
        note: String
    ): Accounting {
        return Accounting(
            id = 0,
            amount = amount,
            category = TransactionCategory.CASH,
            transactionType = TransactionType.DEBIT,
            debitAccount = debitAcc,
            creditAccount = creditAcc,
            timestamp = timestamp,
            note = note
        )
    }
}