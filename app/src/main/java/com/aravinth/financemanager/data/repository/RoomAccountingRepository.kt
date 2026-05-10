package com.aravinth.financemanager.data.repository

import android.R.attr.category
import com.aravinth.financemanager.data.local.AccountingDao
import com.aravinth.financemanager.data.local.AccountingEntity
import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomAccountingRepository @Inject constructor(
    private val dao: AccountingDao
) {
   suspend fun addTransaction(item: AccountingEntity) {
        dao.addTransaction(item)
    }

    fun viewTransactions(): Flow<List<AccountingEntity>> {
        return dao.viewTransactions()
    }

    fun viewByType(type: String): Flow<List<AccountingEntity>> {
        return dao.viewByType(type)
    }

    fun viewByCategory(category: String): Flow<List<AccountingEntity>> {
        return dao.viewByCategory(category)
    }

    suspend fun deleteTransaction(item: AccountingEntity) {
        dao.deleteTransaction(item)
    }
}