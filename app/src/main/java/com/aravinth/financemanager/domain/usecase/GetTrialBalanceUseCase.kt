package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.TrialBalanceReport
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import kotlin.math.abs

class GetTrialBalanceUseCase @Inject constructor(
    private val getLedgerAccountsUseCase: GetLedgerAccountsUseCase
) {
    operator fun invoke(): Flow<TrialBalanceReport> {
        return getLedgerAccountsUseCase().map {ledgerAccounts ->
            //Accounts with a net balance are eligible to be picked for the TB:
            val activeAccounts = ledgerAccounts.filter{ it.netBalance != 0.0 }
            //Positive net balances are debits:
            val grandTotalDebit = activeAccounts.filter{ it.netBalance > 0 }.sumOf{ it.netBalance }
            //Negative net balances are credits (abs() is used to display it in positive in UI):
            val grandTotalCredit = activeAccounts.filter{ it.netBalance < 0 }.sumOf{abs(it.netBalance)}

            TrialBalanceReport(
                activeAccounts = activeAccounts,
                totalDebit = grandTotalDebit,
                totalCredit = grandTotalCredit,
                isBalanced = String.format(Locale.US, "%.2f", grandTotalDebit) ==  String.format(Locale.US, "%.2f", grandTotalCredit)
            )
        }
    }
}