package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.viewmodel.AccountingViewModel
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeStatementScreen(navController: NavController,
                          viewModel: AccountingViewModel = hiltViewModel()
) {
 val report by viewModel.incomeStatementReport.collectAsState(initial = null)

    Scaffold(
        topBar = {TopAppBar(
            title = {Text("Income Statement", fontWeight = FontWeight.Bold)},
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }) {
                     Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }
  ) {innerPadding ->
       report?.let { finalReport ->
           LazyColumn(modifier = Modifier.fillMaxSize()
               .padding(innerPadding)
               .padding(horizontal = 16.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
           ){
               //--Revenue--
               item{SectionHeader("Operating Revenue")}
               items(finalReport.revenues){ account ->
                   AccountRow(account.accountName, abs(account.netBalance))
               }
               item{
                   if(finalReport.revenues.isEmpty()) AccountRow("No Revenue Recorded", 0.0)
                   SubtotalRow("Total Revenue", finalReport.totalRevenue)
               }

               //--COGS--
               item{ Spacer(modifier = Modifier.height(8.dp))}
               item{ SectionHeader("Cost of Goods Sold (COGS)")}
               items(finalReport.cogs) {account ->
                   AccountRow(account.accountName, account.netBalance)
               }
               item{
                   if(finalReport.cogs.isEmpty()) AccountRow("No COGS Recorded", 0.0)
                       SubtotalRow("Total COGS", finalReport.totalCogs)
               }

               //--GROSS profit margin--
               item{ MarginRow("GROSS PROFIT", finalReport.grossProfit, MaterialTheme.colorScheme.primaryContainer)}

               //--OPERATING expenses--
               item{ Spacer(modifier = Modifier.height(8.dp))}
               item{ SectionHeader("Operating Expenses")}
               items(finalReport.operatingExpenses) {account ->
                   AccountRow(account.accountName, account.netBalance)
               }
               item{
                   if (finalReport.operatingExpenses.isEmpty()) AccountRow("No Operating Expenses", 0.0)
                   SubtotalRow("Total Operating Expenses", finalReport.totalOperatingExpense)
               }

               //--EBIT margin--
               item{ MarginRow("EBIT (Operating Profit)", finalReport.ebit, MaterialTheme.colorScheme.secondaryContainer) }

               //--Interest & Taxes--
               item{ Spacer(modifier = Modifier.height(8.dp))}
               item{ SectionHeader("Interest & Taxes") }
               items(finalReport.interestAndTaxes){account ->
                   AccountRow(account.accountName, account.netBalance)
               }
               item{
                   if (finalReport.interestAndTaxes.isEmpty()) AccountRow("No Interest/Taxes", 0.0)
                   SubtotalRow("Total Interest & Taxes", finalReport.totalInterestAndTaxes)
               }

               //--NET profit margin--
               item{ Spacer(modifier = Modifier.height(8.dp))}
               item{
                   val netProfitColor = if (finalReport.netProfit >= 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer
                   MarginRow("NET PROFIT", finalReport.netProfit, netProfitColor, isFinal = true)
               }

               item{ Spacer(modifier = Modifier.height(32.dp))}
           }
       }
    }
}

@Composable
fun SectionHeader(title: String){
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
    HorizontalDivider(thickness = 1.dp)
}

@Composable
fun AccountRow(accountName: String,  amount: Double){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = accountName, style = MaterialTheme.typography.bodyMedium)
        Text(text = "%.2f".format(Locale.US, amount), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SubtotalRow(label: String, amount: Double){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Text(text = "%.2f".format(Locale.US, amount), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun MarginRow(label: String, amount: Double, backgroundColor: androidx.compose.ui.graphics.Color, isFinal: Boolean = false){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp).background(backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = label,
            style = if(isFinal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold
        )
        Text(text = "₹ %.2f".format(Locale.US, amount),
            style = if(isFinal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold)
    }
}