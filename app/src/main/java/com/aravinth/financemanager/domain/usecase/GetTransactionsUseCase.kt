package com.aravinth.financemanager.domain.usecase

import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.domain.model.TransactionType
import com.aravinth.financemanager.domain.repository.AccountingRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: AccountingRepo
) {
    operator fun invoke(
        type: TransactionType? = null,
        category: TransactionCategory? = null
    ): Flow<List<Accounting>> {
        val dataFlow = when {
            type != null -> repository.viewByType(type)
            category != null -> repository.viewByCategory(category)
            else -> repository.viewAll()
        }

      return dataFlow.map { list ->
            list.sortedByDescending { it.timestamp }
        }
    }
}