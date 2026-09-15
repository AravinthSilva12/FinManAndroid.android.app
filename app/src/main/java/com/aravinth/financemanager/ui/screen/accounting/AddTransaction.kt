package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.ui.navigation.Screen
import com.aravinth.financemanager.viewmodel.AccountingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
) {
    val focusManager: FocusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = viewModel.selectedDateMillis)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // 1. TopAppBar properly moved to the topBar parameter
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Transaction",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate(Screen.CoaScreen)}) {
                        Icon(imageVector = Icons.Default.AccountBalance,
                            contentDescription = "Chart of Accounts",
                            modifier = Modifier.padding(end = 4.dp))
                        Text("COA")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        // 2. Column now successfully wraps ALL form fields
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp) // Added better side padding
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp), // Increased spacing between cards
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Card 1: Transaction Type
            FormFieldCard(label = "Transaction type :") {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = viewModel.categoryInput.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        TransactionCategory.entries.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(text = selection.name) },
                                onClick = {
                                    viewModel.onCategorySelect(selection)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Card 2: Amount
            FormFieldCard(label = "Amount :") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.amountInput,
                    onValueChange = { newText -> viewModel.onAmountChange(newText) },
                    label = { Text("Amount") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
            }

            // Card 3: Debit Account
            FormFieldCard(label = "Debit account :") {
                SearchableAccountDropdown(
                    label = "Debit account",
                    selectedAccount = viewModel.debitAccountInput,
                    onAccountSelectChange = { viewModel.onDebitChange(it) },
                    searchQuery = viewModel.debitSearchQuery,
                    onSearchQueryChange = { viewModel.onDebitSearchQueryChange(it) },
                    accountsList = viewModel.availableAccounts,
                    onAddNewAccountClick = {
                        navController.navigate(Screen.CoaScreen)
                    }
                )
            }

            // Card 4: Credit Account
            FormFieldCard(label = "Credit Account :") {
                SearchableAccountDropdown(
                    label = "Credit Account",
                    selectedAccount = viewModel.creditAccountInput,
                    onAccountSelectChange = { viewModel.onCreditChange(it) },
                    searchQuery = viewModel.creditSearchQuery,
                    onSearchQueryChange = { viewModel.onCreditSearchQueryChange(it) },
                    accountsList = viewModel.availableAccounts,
                    onAddNewAccountClick = {
                        navController.navigate(Screen.CoaScreen)
                    }
                )
            }

            // Card 5: Date
            FormFieldCard(label = "Date :") {
                val formattedDate = remember(viewModel.selectedDateMillis) {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(Date(viewModel.selectedDateMillis))
                }
                OutlinedTextField(
                    value = formattedDate,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                        }
                    }
                )
            }

            // Card 6: Note
            FormFieldCard(label = "Note :") {
                OutlinedTextField(
                    value = viewModel.noteInput,
                    onValueChange = { viewModel.onNoteChange(it) },
                    placeholder = { Text("Add transaction notes...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button place (at bottom of form)
            Button(
                onClick = {
                    viewModel.onAddTransaction()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp) // Slightly taller for a better tap target
            ) {
                Text(
                    "Save Transaction",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        } // 3. End of Column

        // Pop-up: Date picker
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.onDateChange(it)
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun FormFieldCard(
    label: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            content()
        }
    }
}