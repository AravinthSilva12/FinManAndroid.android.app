package com.aravinth.financemanager.domain.repository

import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface AccountingRepo {
    suspend fun addTransaction(item: Accounting)

    fun viewByType(type: TransactionType): Flow<List<Accounting>>

    fun viewByCategory(category: TransactionCategory): Flow<List<Accounting>>

    fun viewAll(): Flow<List<Accounting>>

    suspend fun deleteTransaction(item: Accounting)

}