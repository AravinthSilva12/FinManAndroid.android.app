package com.aravinth.financemanager.domain.model


enum class AccountType {
    DRAWINGS,
    DIVIDEND,
    EXPENSE,
    ASSET,
    LIABILITY,
    EQUITY,
    REVENUE
}

enum class AccountCategory{
    //Assets:
    CURRENT_ASSET,
    FIXED_ASSET,
    INTANGIBLE_ASSET,
    NON_CURRENT_ASSET,

    //Liabilities:
    CURRENT_LIABILITY,
    LONG_TERM_LIABILITY,

    //Equity:
    EQUITY,
    OWNER_CAPITAL,
    RETAINED_EARNINGS,

    //Revenue:
    DIRECT_REVENUE,
    OPERATING_REVENUE,
    NON_OPERATING_REVENUE,
    INDIRECT_REVENUE,

    //Expenses:
    COST_OF_GOODS_SOLD,
    DIRECT_EXPENSE,
    OPERATING_EXPENSE,
    NON_OPERATING_EXPENSE
}