package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.ui.navigation.Screen
import com.aravinth.financemanager.viewmodel.AccountingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccountingScreen(
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
) {
    // 1. Collect all necessary data streams from ViewModel
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
    val balanceSheet by viewModel.balanceSheetReport.collectAsState(initial = null)
    val incomeStatement by viewModel.incomeStatementReport.collectAsState(initial = null)
    val ledgers by viewModel.ledgerSummaries.collectAsState(initial = emptyList())

    // 2. Extract Data for Dashboard Cards
    val cashBalance = ledgers.find { it.accountName == "Cash a/c" }?.netBalance ?: 0.0
    val bankBalance = ledgers.find { it.accountName == "Bank a/c" }?.netBalance ?: 0.0

    val totalAssets = balanceSheet?.totalAssets ?: 0.0
    val totalLiabilities = balanceSheet?.totalLiabilities ?: 0.0
    val totalEquity = (balanceSheet?.totalLiabilitiesAndEquity ?: 0.0) - totalLiabilities

    val totalRev = incomeStatement?.totalRevenue ?: 0.0
    val totalExp = (incomeStatement?.totalCogs ?: 0.0) + (incomeStatement?.totalOperatingExpense ?: 0.0) + (incomeStatement?.totalInterestAndTaxes ?: 0.0)
    val netProfit = incomeStatement?.netProfit ?: 0.0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(2, 4, 2, 4),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddTransaction) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 88.dp)
        ) {
            // --- SECTION 1: QUICK NAVIGATION RIBBON ---
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { NavChip("Journal", Icons.Default.Book) { navController.navigate(Screen.Journal()) } }
                    item { NavChip("Ledger", Icons.Default.LibraryBooks) { navController.navigate(Screen.Ledger) } }
                    item { NavChip("Reports", Icons.Default.Assessment) { navController.navigate(Screen.FinancialReport) } }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // --- SECTION 2: THE FUNDAMENTAL EQUATION (Net Worth) ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Net Worth (Equity)",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "₹${"%.2f".format(totalEquity)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Assets", style = MaterialTheme.typography.labelMedium)
                                Text("₹${"%.2f".format(totalAssets)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total Liabilities", style = MaterialTheme.typography.labelMedium)
                                Text("₹${"%.2f".format(totalLiabilities)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // --- SECTION 3: LIQUIDITY SNAPSHOT ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Cash on Hand",
                        amount = cashBalance,
                        icon = Icons.Default.AccountBalanceWallet,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                    DashboardMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Bank Balance",
                        amount = bankBalance,
                        icon = Icons.Default.AccountBalance,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }

            // --- SECTION 4: PERFORMANCE ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("All-Time Performance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Revenue", style = MaterialTheme.typography.labelMedium)
                                Text("₹${"%.2f".format(totalRev)}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Expenses", style = MaterialTheme.typography.labelMedium)
                                Text("₹${"%.2f".format(totalExp)}", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Net Profit", style = MaterialTheme.typography.labelMedium)
                                Text("₹${"%.2f".format(netProfit)}", fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- SECTION 5: RECENT TRANSACTIONS ---
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Activity",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(
                        onClick = { navController.navigate(Screen.Journal()) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("View All")
                    }
                }
            }

            items(transactions.take(5)) { singleTransaction ->
                val formattedDate = remember(singleTransaction.timestamp) {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(singleTransaction.timestamp))
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { navController.navigate(Screen.Journal(targetTransactionId = singleTransaction.id)) }),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = singleTransaction.category.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "₹${"%.2f".format(singleTransaction.amount)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun NavChip(label: String, icon: ImageVector, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun DashboardMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    icon: ImageVector,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.contentColorFor(containerColor).copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.contentColorFor(containerColor).copy(alpha = 0.8f)
            )
            Text(
                text = "₹${"%.2f".format(amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.contentColorFor(containerColor)
            )
        }
    }
}