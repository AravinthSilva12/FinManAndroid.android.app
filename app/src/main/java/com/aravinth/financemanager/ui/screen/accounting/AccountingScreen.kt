package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
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
    Scaffold(modifier = Modifier.fillMaxSize(), contentWindowInsets = WindowInsets(2, 4, 2, 4),
        floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddTransaction) })
                        {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction"
                    )
                }
        }
    )
    {innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { navController.navigate(Screen.Journal) }) {
                        Text("Journal", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(18.dp))
                    }

                    TextButton(onClick = { navController.navigate(Screen.Ledger) }) {
                        Text("Ledger", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(18.dp))
                    }

                    TextButton(onClick = { navController.navigate(Screen.FinancialReport) }) {
                        Text("Report", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Card(modifier = Modifier.fillMaxWidth().height(250.dp).padding(horizontal = 8.dp)) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Dash board space")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 8.dp),
                 verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    item{
                        Text("Recent transactions:", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                items(transactions){singeTraction ->
                    Card(modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clickable(onClick = {navController.navigate(Screen.Journal)})
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 16.dp)
                        ) {
                            Text(text = "Amount: ${singeTraction.amount}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}


