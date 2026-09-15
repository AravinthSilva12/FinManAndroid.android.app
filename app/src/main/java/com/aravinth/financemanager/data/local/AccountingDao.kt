package com.aravinth.financemanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(item: AccountingEntity)

    @Query("SELECT * FROM accounting_table ORDER BY timestamp DESC")
    fun viewTransactions(): Flow<List<AccountingEntity>>

    @Query("SELECT * FROM accounting_table WHERE transactionType = :type")
    fun viewByType(type: String): Flow<List<AccountingEntity>>

    @Query("SELECT * FROM accounting_table WHERE category = :category")
    fun viewByCategory(category: String): Flow<List<AccountingEntity>>

    @Query("SELECT * FROM accounting_table WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun viewTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<AccountingEntity>>

    @Delete
    suspend fun deleteTransaction(item: AccountingEntity)

    //COA:
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccount(account: ChartOfAccountEntity)

    @Query("SELECT * FROM coa_table ORDER BY accountName ASC")
    fun getAllAccounts(): Flow<List<ChartOfAccountEntity>>
}