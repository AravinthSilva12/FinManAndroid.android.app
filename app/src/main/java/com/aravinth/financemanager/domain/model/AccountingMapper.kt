package com.aravinth.financemanager.domain.model

import com.aravinth.financemanager.data.local.AccountingEntity

    fun AccountingEntity.toDomain(): Accounting {
        return Accounting(
         id = this.id,
         amount = this.amount,
         category = TransactionCategory.valueOf(this.category),
         debitAccount = this.debitAccount,
         creditAccount = this.creditAccount,
         transactionType = TransactionType.valueOf(this.transactionType),
         timestamp = this.timestamp,
         note = this.note
        )
    }

    fun Accounting.toEntity(): AccountingEntity {
        return AccountingEntity(
          id = this.id,
          amount = this.amount,
          category = this.category.name,
          debitAccount = this.debitAccount,
          creditAccount = this.creditAccount,
          transactionType = this.transactionType.name,
          timestamp = this.timestamp,
          note = this.note
        )
    }