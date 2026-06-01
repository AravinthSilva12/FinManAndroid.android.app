package com.aravinth.financemanager.viewmodel

import androidx.compose.runtime.getValue
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
                 type = typeInput,
                 timestamp = System.currentTimeMillis()
             )

             viewModelScope.launch {
                 addTransactionUseCase(newEntry)
                 amountInput = ""
             }
         }
    }

    //Debit input:
    fun onDebitChange(newInput: String){
         debitAccountInput = newInput
    }

    //Credit input:
    fun onCreditInput(newInput: String){
        creditAccountInput = newInput
    }

    fun onDeleteTransaction(item: Accounting) {
         viewModelScope.launch {
             deleteTransactionUseCase(item)
         }
    }
}
