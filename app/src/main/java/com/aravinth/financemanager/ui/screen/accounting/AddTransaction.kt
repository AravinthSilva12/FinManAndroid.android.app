package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.domain.model.TransactionCategory
import com.aravinth.financemanager.viewmodel.AccountingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(navController: NavController,
                   viewModel: AccountingViewModel = hiltViewModel()) {
                   val focusManager: FocusManager = LocalFocusManager.current
                   var expanded by remember { mutableStateOf(false) }
                   var showDialog by remember {mutableStateOf(false)}
                   var newAccountName by remember {mutableStateOf("")}
                   var isDebitTarget by remember {mutableStateOf(true)}

  Scaffold(modifier = Modifier.fillMaxSize().padding(4.dp)){
      Column(modifier = Modifier.fillMaxSize().padding(it),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {

          //Prime entry type select:
          ExposedDropdownMenuBox(
              expanded = expanded,
              onExpandedChange = {expanded = !expanded}
          ) {
              OutlinedTextField(
                  value = viewModel.categoryInput.name,
                  onValueChange = {},
                  readOnly = true,
                  label = { Text("Category") },
                  modifier = Modifier.menuAnchor()
              )

              ExposedDropdownMenu(
               expanded = expanded,
                  onDismissRequest = {expanded = false}
              ) {
                  TransactionCategory.entries.forEach { selection->
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

          Spacer(modifier = Modifier.height(8.dp))

          //Amount field:
          OutlinedTextField(value = viewModel.amountInput,
              onValueChange = {newText-> viewModel.onAmountChange(newText)},
              label = { Text("Amount") },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,imeAction = ImeAction.Next),
              keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}))

          Spacer(modifier = Modifier.height(8.dp))

          //Debit account Dropdown brick:
          SearchableAccountDropdown(
              label = "Debit account",
              selectedAccount = viewModel.debitAccountInput,
              onAccountSelectChange = {viewModel.onDebitChange(it)},
              searchQuery = viewModel.debitSearchQuery,
              onSearchQueryChange = {viewModel.onDebitSearchQueryChange(it)},
              accountsList = viewModel.availableAccounts,
              onAddNewAccountClick = {isDebitTarget = true
                                      showDialog = true
              }
          )

          Spacer(modifier = Modifier.height(8.dp))

          //Credit account Dropdown brick:
          SearchableAccountDropdown(
              label = "Credit Account",
              selectedAccount = viewModel.creditAccountInput,
              onAccountSelectChange = {viewModel.onCreditChange(it)},
              searchQuery = viewModel.creditSearchQuery,
              onSearchQueryChange = {viewModel.onCreditSearchQueryChange(it)},
              accountsList = viewModel.availableAccounts,
              onAddNewAccountClick = {
                  isDebitTarget = false
                  showDialog = true
              }
          )

          Spacer(modifier = Modifier.height(8.dp))

          //Button place (at bottom of form):
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Button(onClick = {
                  viewModel.onAddTransaction()
                  navController.popBackStack()
              }
              ) {
                  Text("Save Transaction")
              }
          }

          //Add new account pop-up dialog:
          if(showDialog){
             AlertDialog(
                 onDismissRequest = {showDialog = false},
                 title = {Text("Add New Account")},
                 text = {
                     OutlinedTextField(
                         value = newAccountName,
                         onValueChange = {newAccountName = it},
                         label = {Text("Account Name")},
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
  }
}