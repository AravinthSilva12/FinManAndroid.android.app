package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.repository.AccountingRepo
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: AccountingRepo) {

    suspend operator fun invoke(item: Accounting) {
        repository.deleteTransaction(item)
    }

}