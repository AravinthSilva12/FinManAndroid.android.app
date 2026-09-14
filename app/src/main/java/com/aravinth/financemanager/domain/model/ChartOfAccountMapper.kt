package com.aravinth.financemanager.domain.model

import com.aravinth.financemanager.data.local.ChartOfAccountEntity

fun ChartOfAccountEntity.toDomain(): ChartOfAccount {
    return ChartOfAccount(
        id = this.id,
        accountName = this.accountName,
        accountType = AccountType.valueOf(this.accountType),
        accountCategory = AccountCategory.valueOf(this.accountCategory),
        additionalInfo = this.additionalInfo
    )
}

fun ChartOfAccount.toEntity(): ChartOfAccountEntity{
    return ChartOfAccountEntity(
        id = this.id,
        accountName = this.accountName,
        accountType = this.accountType.name,
        accountCategory = this.accountCategory.name,
        additionalInfo = this.additionalInfo
    )
}