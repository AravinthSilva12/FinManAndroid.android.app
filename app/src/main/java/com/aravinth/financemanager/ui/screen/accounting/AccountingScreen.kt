package com.aravinth.financemanager.ui.screen.accounting

import android.graphics.Paint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Screen.AddTransaction) },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add Transaction") }
                )
        }
    )
    {
        Column(modifier = Modifier.fillMaxSize().padding(it)) {

                Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = { navController.navigate(Screen.Journal)}) {
                    Text("Journal")
                }

                TextButton(onClick = { navController.navigate(Screen.Ledger)}) {
                    Text("Ledger")
                }

                TextButton(onClick = { navController.navigate(Screen.FinancialReport) }) {
                    Text("Financial report")
                }
            }
                Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth().height(150.dp).padding(horizontal = 8.dp)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                     Text("Dash board space")
                }
            }

                Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 8.dp),
                contentPadding = PaddingValues(bottom = 80.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item{
                        Text("Recent transactions:", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                items(transactions){singeTraction ->
                    Card(modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clickable(onClick = {navController.navigate(Screen.Journal)})
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()
                            .padding(16.dp)
                        ) {
                            Text(text = "Amount: ${singeTraction.amount}",
                                style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}


