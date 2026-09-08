package com.aravinth.financemanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {
    //state variables:
         var amountInput by  mutableStateOf("")
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

    //data stream, getTransactionUseCase is operator invoke() type
    val transactions = getTransactionsUseCase()

    //UI event:
    fun onAmountChange(newValue: String) {
        amountInput = newValue
    }

    fun onCategorySelect(category: TransactionCategory){
        categoryInput = category
    }

    fun onAddTransaction() {
        val amount = amountInput.toDoubleOrNull() ?: 0.0

         if(amount <= 0) {
             println("Error! Entered value should be greater than Zero")
         } else {
             val newEntry = Accounting(
                 id = 0,
                 amount = amount,
                 category = categoryInput,
                 transactionType = typeInput,
                 debitAccount = debitAccountInput,
                 creditAccount = creditAccountInput,
                 timestamp = System.currentTimeMillis()
             )

             viewModelScope.launch {
                 addTransactionUseCase(newEntry)
                 amountInput = ""
                 debitAccountInput = ""
                 creditAccountInput = ""
                 debitSearchQuery = ""
                 creditSearchQuery = ""
             }
         }
    }

    //Debit input:
    fun onDebitChange(newInput: String){
        debitAccountInput = newInput
    }

    //Credit input:
    fun onCreditChange(newInput: String){
        creditAccountInput = newInput
    }

    //Search query input:
    fun onSearchQuery(newQuery: String){
           searchQueryInput = newQuery
    }

    //Account listing:
    fun addingNewAccount(addNewAccount: String, isDebitSide: Boolean){
        if(addNewAccount.isNotBlank() && !availableAccounts.contains(addNewAccount)){
            availableAccounts.add(addNewAccount)
        }
        if(isDebitSide){
            debitAccountInput = addNewAccount
        } else{
           creditAccountInput = addNewAccount
        }
    }

    fun onDeleteTransaction(item: Accounting) {
         viewModelScope.launch {
             deleteTransactionUseCase(item)
         }
    }

    fun onDebitSearchQueryChange(newQuery: String){
          debitSearchQuery = newQuery
    }

    fun onCreditSearchQueryChange(newQuery: String){
          creditSearchQuery = newQuery
    }

    fun onNoteChange(newNote: String){
        noteInput = newNote
    }

    fun onDateChange(newDateMillis: Long){
        selectedDateMillis = newDateMillis
    }
}
