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
    CURRENT_ASSET,
    FIXED_ASSET,

    CURRENT_LIABILITY,
    LONG_TERM_LIABILITY,

    OWNER_CAPITAL,
    RETAINED_EARNINGS,

    DIRECT_REVENUE,
    INDIRECT_REVENUE,

    DIRECT_EXPENSE,
    OPERATING_EXPENSE
}