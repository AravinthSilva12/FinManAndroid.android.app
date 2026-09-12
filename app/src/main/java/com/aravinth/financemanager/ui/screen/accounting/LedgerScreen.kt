package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
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
import com.aravinth.financemanager.domain.model.AccountSummary
import com.aravinth.financemanager.ui.navigation.Screen
import com.aravinth.financemanager.viewmodel.AccountingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerScreen(navController: NavController,
                 viewModel: AccountingViewModel = hiltViewModel()
) {
    val ledgerSummaries by viewModel.ledgerSummaries.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "General Ledger",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp)
            )
        }
    ) { innerPadding ->
        if(ledgerSummaries.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "No ledger accounts available.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }
        } else{
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                items(
                    items = ledgerSummaries,
                    key = { it.accountName }
                ){summary ->
                    LedgerAccountCard(
                        summary = summary,
                        onClick = {
                             navController.navigate(Screen.TAccountDetail(accountName = summary.accountName))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LedgerAccountCard(
    summary: AccountSummary,
    onClick: () -> Unit
){
    val isPositive = summary.netBalance >= 0

    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ){
          Column(modifier = Modifier.padding(16.dp)) {
              Row(modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
              ){
                  Text(
                      text = summary.accountName,
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Bold,
                      color = colorScheme.primary
                  )
                  Text(
                      text = "₹%.2f".format(summary.netBalance),
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.ExtraBold,
                      color = if (isPositive) colorScheme.onSurface else colorScheme.error
                  )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
              ){
                  Text(
                      text = "Dr: ₹%.2f".format(summary.totalDebit),
                      style = MaterialTheme.typography.bodyMedium,
                      fontWeight = FontWeight.Medium,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                      text = "Cr: ₹%.2f".format(summary.totalCredit),
                      style = MaterialTheme.typography.bodyMedium,
                      fontWeight = FontWeight.Medium,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
              }
          }
    }
}