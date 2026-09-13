package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrialBalanceScreen(
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
) {
    val report by viewModel.trialBalanceReport.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
            title = {Text("Trial Balance", fontWeight = FontWeight.Bold)},
                navigationIcon = {IconButton(
                    onClick = {navController.popBackStack()}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
             }
         )
        },
        bottomBar = {
            report?.let { finalReport ->
                TrialBalanceFooter(
                    totalDebit = finalReport.totalDebit,
                        totalCredit = finalReport.totalCredit,
                            isBalanced = finalReport.isBalanced
                )
            }
        }
        ){innerPadding ->
          report?.let { finalReport ->
              Column(
                  modifier = Modifier.fillMaxSize()
                      .padding(innerPadding)
                      .padding(horizontal = 8.dp)
              ){
                  //Header row:
                  Row(
                      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                      horizontalArrangement = Arrangement.SpaceBetween
                  ){
                     Text("Particulars", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                     Text("Debit (₹)", modifier = Modifier.weight(1.2f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                     Text("Credit (₹)", modifier = Modifier.weight(1.2f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                  }

                  HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)

                  //Accounts List:
                  LazyColumn(modifier = Modifier.weight(1f)){
                       items(finalReport.activeAccounts){account ->
                           val isDebit = account.netBalance > 0
                           TrialBalanceRow(
                               accountName = account.accountName,
                               debitAmount = if(isDebit) account.netBalance else null,
                               creditAmount = if(!isDebit) abs(account.netBalance) else null
                           )
                       }
                  }
              }
          }
    }
}

@Composable
fun TrialBalanceRow(accountName: String, debitAmount: Double?, creditAmount: Double?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
          Text(text = accountName,
              modifier = Modifier.weight(2f),
              style = MaterialTheme.typography.bodyMedium)

          Text(text = debitAmount?.let {"%.2f".format(java.util.Locale.US, it)} ?: "-",
              modifier = Modifier.weight(1.2f),
              textAlign = TextAlign.End,
              style = MaterialTheme.typography.bodyMedium)

          Text(text = creditAmount?.let{"%.2f".format(java.util.Locale.US, it)} ?: "-",
              modifier = Modifier.weight(1.2f),
              textAlign = TextAlign.End,
              style = MaterialTheme.typography.bodyMedium)
    }
    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
fun TrialBalanceFooter(totalDebit: Double, totalCredit: Double, isBalanced: Boolean){
val  containerColor = if(isBalanced) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
val contentColor = if(isBalanced) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
    Column(
        modifier = Modifier.fillMaxWidth().background(containerColor).padding(16.dp).navigationBarsPadding()
    ){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("GRAND TOTAL", modifier = Modifier.weight(2f), fontWeight = FontWeight.ExtraBold, color = contentColor)
            Text("%.2f".format(java.util.Locale.US, totalDebit), modifier = Modifier.weight(1.2f), textAlign = TextAlign.End,
                fontWeight = FontWeight.ExtraBold, color = contentColor)
            Text("%.2f".format(java.util.Locale.US, totalCredit), modifier = Modifier.weight(1.2f), textAlign = TextAlign.End,
                fontWeight = FontWeight.ExtraBold, color = contentColor)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if(isBalanced) "Books are perfectly balanced." else "Warning! - Unbalanced ledgers detected",
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
