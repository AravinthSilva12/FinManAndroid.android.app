package com.aravinth.financemanager.data.repository

import com.aravinth.financemanager.data.local.AccountingDao
import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.domain.model.TransactionType
import com.aravinth.financemanager.domain.model.toDomain
import com.aravinth.financemanager.domain.model.toEntity
import com.aravinth.financemanager.domain.repository.AccountingRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomAccountingRepository @Inject constructor(
    private val dao: AccountingDao
) : AccountingRepo {
    override suspend fun addTransaction(item: Accounting) {
        dao.addTransaction(item.toEntity())
    }

    override fun viewAll(): Flow<List<Accounting>> {
        return dao.viewTransactions().map{list -> list.map{it.toDomain()}}
    }

    override fun viewByType(type: TransactionType): Flow<List<Accounting>> {
        return dao.viewByType(type.name).map { list -> list.map{it.toDomain()} }
    }

    override fun viewByCategory(category: TransactionCategory): Flow<List<Accounting>> {
        return dao.viewByCategory(category.name).map{list-> list.map {it.toDomain()}}
    }

    override suspend fun deleteTransaction(item: Accounting) {
        dao.deleteTransaction(item.toEntity())
    }
}