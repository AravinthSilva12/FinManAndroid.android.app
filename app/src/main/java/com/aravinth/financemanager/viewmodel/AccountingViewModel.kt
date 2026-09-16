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
import com.aravinth.financemanager.domain.usecase.CloseAccountingPeriodUseCase
import com.aravinth.financemanager.domain.usecase.DeleteTransactionUseCase
import com.aravinth.financemanager.domain.usecase.GetBalanceSheetUseCase
import com.aravinth.financemanager.domain.usecase.GetIncomeStatementUseCase
import com.aravinth.financemanager.domain.usecase.GetLedgerAccountsUseCase
import com.aravinth.financemanager.domain.usecase.GetTAccountUseCase
import com.aravinth.financemanager.domain.usecase.GetTransactionsUseCase
import com.aravinth.financemanager.domain.usecase.GetTrialBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

enum class DateFilter { TODAY, THIS_MONTH, ALL, CUSTOM_DATE } // Added CUSTOM_DATE

@HiltViewModel
class AccountingViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val accountRepository: RoomAccountingRepository,
    private val getLedgerAccountsUseCase: GetLedgerAccountsUseCase,
    private val getTAccountUseCase: GetTAccountUseCase,
    private val getTrialBalanceUseCase: GetTrialBalanceUseCase,
    private val getIncomeStatementUseCase: GetIncomeStatementUseCase,
    private val getBalanceSheetUseCase: GetBalanceSheetUseCase,
    private val closeAccountingPeriodUseCase: CloseAccountingPeriodUseCase

) : ViewModel() {

    //Filter State:
    val currentFilter = MutableStateFlow(DateFilter.ALL)
    var showFilterChips by mutableStateOf(false)
    var customFilterDateMillis by mutableLongStateOf(System.currentTimeMillis()) // Custom date state

    //Transaction form state:
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
    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = currentFilter.flatMapLatest {filter ->
        when (filter) {
            DateFilter.TODAY -> {
                val (start, end) = getTodayRange()
                getTransactionsUseCase(startDate = start, endDate = end)
            }
            DateFilter.THIS_MONTH -> {
                val (start, end) = getMonthRange()
                getTransactionsUseCase(startDate = start, endDate = end)
            }
            DateFilter.ALL -> getTransactionsUseCase()
            DateFilter.CUSTOM_DATE -> { // Handle custom date query
                val (start, end) = getCustomDayRange(customFilterDateMillis)
                getTransactionsUseCase(startDate = start, endDate = end)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val balanceSheetReport = currentFilter.flatMapLatest {filter ->
        when (filter) {
            DateFilter.TODAY -> {
                val (start, end) = getTodayRange()
                getBalanceSheetUseCase(startDate = start, endDate = end)
            }
            DateFilter.THIS_MONTH -> {
                val (start, end) = getMonthRange()
                getBalanceSheetUseCase(startDate = start, endDate = end)
            }
            DateFilter.ALL -> getBalanceSheetUseCase()
            DateFilter.CUSTOM_DATE -> {
                val (start, end) = getCustomDayRange(customFilterDateMillis)
                getBalanceSheetUseCase(startDate = start, endDate = end)
            }
        }
    }

    val ledgerSummaries = getLedgerAccountsUseCase()
    val trialBalanceReport = getTrialBalanceUseCase()
    val incomeStatementReport = getIncomeStatementUseCase()
    val allAccounts = accountRepository.getAllAccounts()


    //Filter logic:
    fun setDateFilter(filter: DateFilter) {
        currentFilter.value = filter
    }

    fun setCustomDateFilter(dateMillis: Long) {
        customFilterDateMillis = dateMillis
        currentFilter.value = DateFilter.CUSTOM_DATE
    }

    fun toggleFilterVisibility() {
        showFilterChips = !showFilterChips
    }

    private fun getTodayRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val end = calendar.timeInMillis
        return Pair(start, end)
    }

    private fun getMonthRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val end = calendar.timeInMillis
        return Pair(start, end)
    }

    private fun getCustomDayRange(dateMillis: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return Pair(start, calendar.timeInMillis)
    }

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
                coaCategoryInput = AccountCategory.CURRENT_ASSET
                coaTypeInput = AccountType.ASSET
            }
        }
    }

    //Transaction functions:
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

    fun onCloseAccountingPeriod(closingDateMillis: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            closeAccountingPeriodUseCase(closingDateMillis)
        }
    }

    fun deleteAccountIfUnused(account: ChartOfAccount, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val allTransactions = getTransactionsUseCase().first()

            val isUsed = allTransactions.any {
                it.debitAccount == account.accountName || it.creditAccount == account.accountName
            }
            if (!isUsed) {
                accountRepository.deleteAccount(account)
                onResult(true, "Account deleted successfully.")
            } else {
                onResult(false, "Cannot delete: Account is tied to existing transactions.")
            }
        }
    }
}