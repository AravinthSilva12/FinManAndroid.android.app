package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan // Required for the full-width item
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.ui.navigation.Screen
import com.aravinth.financemanager.viewmodel.AccountingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialReportScreen(
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
) {
    var showCloseDialog by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Financial Reports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        // ENTIRE SCREEN IS NOW ONE SCROLLABLE GRID
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReportDashboardCard(
                    title = "Trial Balance",
                    icon = Icons.Default.Balance,
                    onClick = { navController.navigate(Screen.TrialBalance) }
                )
            }
            item {
                ReportDashboardCard(
                    title = "Income Statement",
                    icon = Icons.Default.EditNote,
                    onClick = { navController.navigate(Screen.IncomeStatement) }
                )
            }
            item {
                ReportDashboardCard(
                    title = "Balance Sheet",
                    icon = Icons.Default.AccountBalance,
                    onClick = { navController.navigate(Screen.BalanceSheetScreen) }
                )
            }

            // POST-CLOSING ACTION SECTION (Spans full width)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp), // Acts as the spacer from the grid above
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 24.dp).padding(horizontal = 8.dp))

                    Text(
                        text = "Year-End Operations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Button(
                        onClick = { showCloseDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("Close Financial Year", style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        text = "Generates closing journal entries to zero out temporary accounts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // --- CONFIRMATION DIALOG ---
        if (showCloseDialog) {
            AlertDialog(
                onDismissRequest = { showCloseDialog = false },
                title = { Text("Close Financial Year?") },
                text = {
                    Text("This will permanently write closing journal entries to zero out all Revenue, Expense, and Dividend accounts, moving their balances to Retained Earnings.\n\nAre you sure you want to proceed?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.onCloseAccountingPeriod()
                            showCloseDialog = false

                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Books Closed Successfully!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Yes, Close Books")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCloseDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ReportDashboardCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}