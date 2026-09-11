package com.aravinth.financemanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aravinth.financemanager.data.repository.RoomAccountingRepository
import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.domain.model.TransactionType
import com.aravinth.financemanager.domain.usecase.AddTransactionUseCase
import com.aravinth.financemanager.domain.usecase.DeleteTransactionUseCase
import com.aravinth.financemanager.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountingViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val accountRepository: RoomAccountingRepository
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
    var selectedDateMillis by mutableStateOf(System.currentTimeMillis())

    init {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { savedList ->
                val allNames = (listOf("Cash a/c", "Bank a/c") + savedList).distinct()
                availableAccounts.clear()
                availableAccounts.addAll(allNames)
            }
        }
    }

    // Data stream
    val transactions = getTransactionsUseCase()

    // Add new account with DB persistence
    fun addingNewAccount(addNewAccount: String, isDebitSide: Boolean) {
        val trimmed = addNewAccount.trim()
        if (trimmed.isNotBlank()) {
            if (!availableAccounts.contains(trimmed)) {
                availableAccounts.add(trimmed)
            }

            viewModelScope.launch {
                accountRepository.insertAccount(trimmed)
            }

            if (isDebitSide) {
                debitAccountInput = trimmed
            } else {
                creditAccountInput = trimmed
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
}