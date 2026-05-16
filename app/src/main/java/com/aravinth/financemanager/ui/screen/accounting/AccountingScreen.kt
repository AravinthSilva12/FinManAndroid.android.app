package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        floatingActionButton = { ExtendedFloatingActionButton(onClick = {navController.navigate(Screen.AddTransaction) },
        icon = { Icon(Icons.Default.Add, contentDescription = null) },
        text = { Text("Add Transaction") } )
        }
    ) {it ->
                LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {

                }
       }
    }

