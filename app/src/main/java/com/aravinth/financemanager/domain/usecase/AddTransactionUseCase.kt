package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.data.local.AccountingEntity
import com.aravinth.financemanager.data.repository.RoomAccountingRepository
import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.repository.AccountingRepo
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: AccountingRepo
) {
    suspend operator fun invoke(item: Accounting) {
        repository.addTransaction(item)
    }
}