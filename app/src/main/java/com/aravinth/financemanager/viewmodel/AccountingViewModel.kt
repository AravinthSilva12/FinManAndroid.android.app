package com.aravinth.financemanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aravinth.financemanager.data.repository.RoomAccountingRepository
import com.aravinth.financemanager.domain.model.AccountCategory
import com.aravinth.financemanager.domain.model.AccountType
import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.model.ChartOfAccount
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.domain.model.TransactionType
import com.aravinth.financemanager.domain.usecase.AddTransactionUseCase
import com.aravinth.financemanager.domain.usecase.DeleteTransactionUseCase
import com.aravinth.financemanager.domain.usecase.GetIncomeStatementUseCase
import com.aravinth.financemanager.domain.usecase.GetLedgerAccountsUseCase
import com.aravinth.financemanager.domain.usecase.GetTAccountUseCase
import com.aravinth.financemanager.domain.usecase.GetTransactionsUseCase
import com.aravinth.financemanager.domain.usecase.GetTrialBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountingViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val accountRepository: RoomAccountingRepository,
    private val getLedgerAccountsUseCase: GetLedgerAccountsUseCase,
    private val getTAccountUseCase: GetTAccountUseCase,
    private val getTrialBalanceUseCase: GetTrialBalanceUseCase,
    private val getIncomeStatementUseCase: GetIncomeStatementUseCase
) : ViewModel() {

    // State variables
    var amountInput by mutableStateOf("")
    var typeInput by mutableStateOf(TransactionType.DEBIT)
    var categoryInput by mutableStateOf(TransactionCategory.CASH)
    var debitAccountInput by mutableStateOf("")
    var creditAccountInput by mutableStateOf("")

    var searchQueryInput by mutableStateOf("")
    var debitSearchQuery by mutableStateOf("")
    var creditSearchQuery by mutableStateOf("")
    val availableAccounts = mutableStateListOf("Cash a/c", "Bank a/c")
    var noteInput by mutableStateOf("")
    var selectedDateMillis by mutableLongStateOf(System.currentTimeMillis())

    //Chart of Accounts form state:
    var coaNameInput by mutableStateOf("")
    var coaTypeInput by mutableStateOf(AccountType.ASSET)
    var coaCategoryInput by mutableStateOf(AccountCategory.CURRENT_ASSET)
    var coaInfoInput by mutableStateOf("")

    init {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { savedList ->
                val allNames = (listOf("Cash a/c", "Bank a/c") + savedList.map{it.accountName}).distinct()
                availableAccounts.clear()
                availableAccounts.addAll(allNames)
            }
        }
    }

    // Data stream
    val transactions = getTransactionsUseCase()
    val ledgerSummaries = getLedgerAccountsUseCase()
    val trialBalanceReport = getTrialBalanceUseCase()
    val incomeStatementReport = getIncomeStatementUseCase()
    val allAccounts = accountRepository.getAllAccounts()

    //COA function:
    fun onCoaNameChange(name: String) { coaNameInput = name }
    fun onCoaTypeChange(type: AccountType) { coaTypeInput = type }
    fun onCoaCategoryChange(category: AccountCategory) { coaCategoryInput = category }
    fun onCoaInfoChange(info: String) { coaInfoInput = info }

    fun savedChartOfAccount() {
        if(coaNameInput.isNotBlank()) {
            viewModelScope.launch {
                val newAccount = ChartOfAccount(
                    accountName = coaNameInput.trim(),
                    accountType = coaTypeInput,
                    accountCategory = coaCategoryInput,
                    additionalInfo = coaInfoInput
                )
                accountRepository.insertAccount(newAccount)
                coaNameInput = ""
                coaInfoInput = ""
            }
        }
    }

    fun onAmountChange(newValue: String) {
        amountInput = newValue
    }

    fun onCategorySelect(category: TransactionCategory) {
        categoryInput = category
    }

    fun onAddTransaction() {
        val amount = amountInput.toDoubleOrNull() ?: 0.0

        if (amount <= 0) {
            println("Error! Entered value should be greater than Zero")
        } else {
            val newEntry = Accounting(
                id = 0,
                amount = amount,
                category = categoryInput,
                transactionType = typeInput,
                debitAccount = debitAccountInput,
                creditAccount = creditAccountInput,
                timestamp = selectedDateMillis,
                note = noteInput
            )

            viewModelScope.launch {
                addTransactionUseCase(newEntry)
                amountInput = ""
                debitAccountInput = ""
                creditAccountInput = ""
                debitSearchQuery = ""
                creditSearchQuery = ""
                noteInput = ""
            }
        }
    }

    fun onDebitChange(newInput: String) {
        debitAccountInput = newInput
    }

    fun onCreditChange(newInput: String) {
        creditAccountInput = newInput
    }

    fun onSearchQuery(newQuery: String) {
        searchQueryInput = newQuery
    }

    fun onDeleteTransaction(item: Accounting) {
        viewModelScope.launch {
            deleteTransactionUseCase(item)
        }
    }

    fun onDebitSearchQueryChange(newQuery: String) {
        debitSearchQuery = newQuery
    }

    fun onCreditSearchQueryChange(newQuery: String) {
        creditSearchQuery = newQuery
    }

    fun onNoteChange(newNote: String) {
        noteInput = newNote
    }

    fun onDateChange(newDateMillis: Long) {
        selectedDateMillis = newDateMillis
    }

    fun getTAccountDetail(accountName: String) = getTAccountUseCase(accountName)
}