package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
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
import com.aravinth.financemanager.viewmodel.AccountingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TAccountScreen(
    accountName: String,
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
    ) {
    //Observe the specific account's detail stream:
    val accountDetail by viewModel.getTAccountDetail(accountName).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$accountName Ledger", fontWeight = FontWeight.Bold)},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){innerPadding ->
        accountDetail?.let {detail ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                // T header:
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Dr (Debit)",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Cr (credit)",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)

                //The split columns:
                Row(modifier = Modifier.weight(1f)) {
                    //Left side : Debits:
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    ) {
                        items(detail.debitEntries) { entry ->
                            TAccountEntryRow(label = entry.creditAccount.ifEmpty { "Cash a/c" },
                                amount = entry.amount,
                                isDebitSide = true)
                        }
                        //If credit side is heavier, balance c/d goes on the debit side
                        if(detail.isCreditBalance && detail.closingBalanceCD > 0){
                            item{
                                TAccountEntryRow(label = "Balance c/d", amount = detail.closingBalanceCD,
                                    isDebitSide = true, isBalance = true)
                            }
                        }
                    }

                    VerticalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)

                    //Right side : Credits:
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    ) {
                      items(detail.creditEntries) { entry ->
                          TAccountEntryRow(label = entry.debitAccount.ifEmpty { "Bank a/c" },
                              amount = entry.amount,
                              isDebitSide = false)
                      }
                     //If debit side is heavier, balance c/d goes on the credit side:
                     if(detail.isDebitBalance && detail.closingBalanceCD > 0 ){
                         item{
                             TAccountEntryRow(label = "Balance c/d", amount = detail.closingBalanceCD,
                                 isDebitSide = false, isBalance = true)
                         }
                     }
                  }
              }

              HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)

              //The bottom totals (always equal):
              Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                  Text(
                      text = "₹%.2f".format(detail.totalDebitSide),
                      modifier = Modifier.weight(1f),
                      textAlign = TextAlign.Center,
                      fontWeight = FontWeight.ExtraBold
                  )
                  Text(
                      text = "₹%.2f".format(detail.totalCreditSide),
                      modifier = Modifier.weight(1f),
                      textAlign = TextAlign.Center,
                      fontWeight = FontWeight.ExtraBold
                  )
           }
        }
     }
  }
}

@Composable
fun TAccountEntryRow(
    label: String,
    amount: Double,
    isDebitSide: Boolean,
    isBalance: Boolean = false) {
     val prefix = if (isBalance) "" else if (isDebitSide) "To " else "By "
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$prefix$label",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if(isBalance) FontWeight.Bold else FontWeight.Normal,
            color = if(isBalance) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f).padding(end = 4.dp)
        )
        Text(
            text = "₹${amount}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if(isBalance) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.End
        )
    }
}
