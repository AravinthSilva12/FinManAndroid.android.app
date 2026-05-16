package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.viewmodel.AccountingViewModel


@Composable
fun AddTransaction(navController: NavController,
                   viewModel: AccountingViewModel = hiltViewModel()) {
                   val focusManager: FocusManager = LocalFocusManager.current

  Scaffold(modifier = Modifier.fillMaxSize().padding(4.dp)){it->
      Column(modifier = Modifier.fillMaxSize().padding(it)) {
          OutlinedTextField(value = viewModel.amountInput, onValueChange = {newText-> viewModel.onAmountChange(newText)}, label = { Text("Transaction") },
              singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
              keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}))

          Spacer(modifier = Modifier.height(4.dp))

          Button(onClick = {viewModel.onAddTransaction()
                            navController.popBackStack()}) {
              Text("Save Transaction")
          }

      }
  }
}