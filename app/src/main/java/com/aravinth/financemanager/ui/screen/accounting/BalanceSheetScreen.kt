package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.domain.model.BalanceSheetItem
import com.aravinth.financemanager.viewmodel.AccountingViewModel
import com.aravinth.financemanager.viewmodel.DateFilter
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceSheetScreen(
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
) {
    // Collect the data stream. We use null as initial state so we can show a loading indicator if needed.
    val report by viewModel.balanceSheetReport.collectAsState(initial = null)
    val currentFilter by viewModel.currentFilter.collectAsState()
    val showFilters = viewModel.showFilterChips

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Balance Sheet", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFilterVisibility() }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // --- FILTER CHIPS ---
            AnimatedVisibility(visible = showFilters) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = currentFilter == DateFilter.TODAY,
                        onClick = { viewModel.setDateFilter(DateFilter.TODAY) },
                        label = { Text("Today") }
                    )
                    FilterChip(
                        selected = currentFilter == DateFilter.THIS_MONTH,
                        onClick = { viewModel.setDateFilter(DateFilter.THIS_MONTH) },
                        label = { Text("This Month") }
                    )
                    FilterChip(
                        selected = currentFilter == DateFilter.ALL,
                        onClick = { viewModel.setDateFilter(DateFilter.ALL) },
                        label = { Text("All Time") }
                    )
                }
            }

            // --- MAIN CONTENT ---
            if (report == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // 1. ASSETS SECTION
                    item {
                        BalanceSheetSection(
                            title = "Assets",
                            items = report!!.assets,
                            totalLabel = "Total Assets",
                            totalAmount = report!!.totalAssets
                        )
                    }

                    // 2. LIABILITIES SECTION
                    item {
                        BalanceSheetSection(
                            title = "Liabilities",
                            items = report!!.liabilities,
                            totalLabel = "Total Liabilities",
                            totalAmount = report!!.totalLiabilities
                        )
                    }

                    // 3. EQUITY SECTION
                    item {
                        val totalEquity = report!!.totalLiabilitiesAndEquity - report!!.totalLiabilities
                        BalanceSheetSection(
                            title = "Equity",
                            items = report!!.equities,
                            totalLabel = "Total Equity",
                            totalAmount = totalEquity
                        )
                    }

                    // 4. TOTAL LIABILITIES & EQUITY (The Balancing check)
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Liab. & Equity",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "₹${"%.2f".format(report!!.totalLiabilitiesAndEquity)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // Accounting Equation check (Assets = Liabilities + Equity)
                        val isBalanced = abs(report!!.totalAssets - report!!.totalLiabilitiesAndEquity) < 0.01
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBalanced) "✅ Balance Sheet is balanced" else "❌ Out of balance",
                            color = if (isBalanced) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

// Reusable component to render each section block cleanly
@Composable
fun BalanceSheetSection(
    title: String,
    items: List<BalanceSheetItem>,
    totalLabel: String,
    totalAmount: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (items.isEmpty()) {
                Text(
                    text = "No recorded accounts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item.accountName, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "₹${"%.2f".format(item.amount)}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = totalLabel, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = "₹${"%.2f".format(totalAmount)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}