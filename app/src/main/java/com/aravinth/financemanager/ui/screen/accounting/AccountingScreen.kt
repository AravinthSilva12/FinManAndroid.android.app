package com.aravinth.financemanager.ui.screen.accounting

import android.R.attr.icon
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.ui.navigation.Screen
import com.aravinth.financemanager.viewmodel.AccountingViewModel

@Composable
fun AccountingScreen (navController: NavController, viewModel: AccountingViewModel = hiltViewModel())
{
    //State variable:
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())

    //Scaffold:
    Scaffold(modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Screen.ViewTransactions) },
                    icon = { Icon(imageVector = Icons.Default.Visibility, contentDescription = null) },
                    text = { Text("View Transactions") }
                )

                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Screen.AddTransaction) },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add Transaction") }
                )
            }
        }
    )
    {it ->
                LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {

                }
       }
    }

