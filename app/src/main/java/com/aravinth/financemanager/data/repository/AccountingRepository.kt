package com.aravinth.financemanager.data.repository

import com.aravinth.financemanager.data.local.AccountingDao
import com.aravinth.financemanager.data.local.AccountingEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountingRepository @Inject constructor(
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

    suspend fun deleteTransaction(item: AccountingEntity) {
        dao.deleteTransaction(item)
    }
}