package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.viewmodel.AccountingViewModel

@Composable
fun ViewTransactions(navController: NavController,
                     viewModel: AccountingViewModel = viewModel()) {
     Box(modifier =  Modifier.fillMaxSize().padding(4.dp)){
         Text("Under construction!", textAlign = TextAlign.Center)
     }
}