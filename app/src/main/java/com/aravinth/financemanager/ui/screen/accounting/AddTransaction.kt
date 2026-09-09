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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
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
import com.aravinth.financemanager.viewmodel.AccountingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(navController: NavController,
                   viewModel: AccountingViewModel = hiltViewModel()) {
    val focusManager: FocusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var newAccountName by remember { mutableStateOf("") }
    var isDebitTarget by remember { mutableStateOf(true) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = viewModel.selectedDateMillis)
    Scaffold(
        modifier = Modifier.fillMaxSize(), contentWindowInsets = WindowInsets(0, 4, 0, 4)
    ) {innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TopAppBar(
                    title = {
                        Text(
                            text = "Financial manager",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )

                //Visual Outer shell (card frame):
                FormFieldCard(label = "Transaction type :") {
                    //Prime entry type select:
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = viewModel.categoryInput.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
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

                //Card2: Amount:
                FormFieldCard(label = "Amount :") {
                    //Amount field:
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
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        })
                    )
                }

                //Card3: Debit Account:
                FormFieldCard(label = "Debit account :") {
                    //Debit account Dropdown brick:
                    SearchableAccountDropdown(
                        label = "Debit account",
                        selectedAccount = viewModel.debitAccountInput,
                        onAccountSelectChange = { viewModel.onDebitChange(it) },
                        searchQuery = viewModel.debitSearchQuery,
                        onSearchQueryChange = { viewModel.onDebitSearchQueryChange(it) },
                        accountsList = viewModel.availableAccounts,
                        onAddNewAccountClick = {
                            isDebitTarget = true
                            showDialog = true
                        }
                    )
                }

                //Card4: Credit Account:
                FormFieldCard(label = "Credit Account :") {
                    //Credit account Dropdown brick:
                    SearchableAccountDropdown(
                        label = "Credit Account",
                        selectedAccount = viewModel.creditAccountInput,
                        onAccountSelectChange = { viewModel.onCreditChange(it) },
                        searchQuery = viewModel.creditSearchQuery,
                        onSearchQueryChange = { viewModel.onCreditSearchQueryChange(it) },
                        accountsList = viewModel.availableAccounts,
                        onAddNewAccountClick = {
                            isDebitTarget = false
                            showDialog = true
                        }
                    )
                }

                //Card5: Date:
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

                //Card6: Note:
                FormFieldCard(label = "Note :") {
                    OutlinedTextField(
                        value = viewModel.noteInput,
                        onValueChange = { viewModel.onNoteChange(it) },
                        placeholder = { Text("Add transaction notes...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Button place (at bottom of form):
                Button(
                    onClick = {
                        viewModel.onAddTransaction()
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(0.85f)
                        .height(46.dp)
                ) {
                    Text(
                        "Save Transaction", fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Add new account pop-up dialog:
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Add New Account") },
                        text = {
                            OutlinedTextField(
                                value = newAccountName,
                                onValueChange = { newAccountName = it },
                                label = { Text("Account Name") },
                                singleLine = true
                            )
                        },
                        confirmButton = {
                            Button(onClick = {
                                viewModel.addingNewAccount(newAccountName, isDebitTarget)
                                newAccountName = ""
                                showDialog = false
                            }) {
                                Text("Add")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }

            //Pop-up: Date picker:
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
                            Text("ok")
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
fun FormFieldCard(label: String,
                  content: @Composable () -> Unit
) {
  Card(modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(10.dp),
      colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
      )
  ) {
      Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)){
          Text(text = label,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(bottom = 2.dp)
          )
          content()
      }
  }
}
