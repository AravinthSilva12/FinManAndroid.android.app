package com.aravinth.financemanager.ui.screen.accounting

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.domain.model.AccountCategory
import com.aravinth.financemanager.domain.model.AccountType
import com.aravinth.financemanager.viewmodel.AccountingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoaScreen(
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
) {
    val accounts by viewModel.allAccounts.collectAsState(initial = emptyList())
    var typeExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var showTypeGuide by remember { mutableStateOf(false) }
    var showCategoryGuide by remember { mutableStateOf(false) }

    // Required for triggering Toast messages in Compose
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chart of Accounts", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Creation form:
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Add New Account", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = viewModel.coaNameInput,
                            onValueChange = { viewModel.onCoaNameChange(it) },
                            label = { Text("Account Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Standard Dropdown for Account type:
                        Box {
                            OutlinedTextField(
                                value = viewModel.coaTypeInput.name,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Account Type (DEALER)") },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { showTypeGuide = true }) {
                                            Icon(Icons.Default.Info, contentDescription = "Type Guide")
                                        }
                                        IconButton(onClick = { typeExpanded = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                                        }
                                    }
                                }
                            )
                            DropdownMenu(
                                expanded = typeExpanded,
                                onDismissRequest = { typeExpanded = false }
                            ) {
                                AccountType.entries.forEach { selection ->
                                    DropdownMenuItem(
                                        text = { Text(selection.name) },
                                        onClick = {
                                            viewModel.onCoaTypeChange(selection)
                                            typeExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Standard Dropdown for Account category:
                        Box {
                            OutlinedTextField(
                                value = viewModel.coaCategoryInput.name,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category") },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { showCategoryGuide = true }) {
                                            Icon(Icons.Default.Info, contentDescription = "Category Guide")
                                        }
                                        IconButton(onClick = { categoryExpanded = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                                        }
                                    }
                                }
                            )
                            DropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                AccountCategory.entries.forEach { selection ->
                                    DropdownMenuItem(
                                        text = { Text(selection.name) },
                                        onClick = {
                                            viewModel.onCoaCategoryChange(selection)
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = viewModel.coaInfoInput,
                            onValueChange = { viewModel.onCoaInfoChange(it) },
                            label = { Text("Additional Info (Optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { viewModel.savedChartOfAccount() },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text("Save Account")
                        }
                    }
                }
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Existing Accounts", style = MaterialTheme.typography.titleMedium)
            }

            // Master List:
            items(accounts) { account ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = account.accountName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Layout updated to align Delete button to the end
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(account.accountType.name) }
                                )
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(account.accountCategory.name) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.deleteAccountIfUnused(account) { success, message ->
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Account",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                )
                            }
                        }

                        if (account.additionalInfo.isNotBlank()) {
                            Text(
                                text = account.additionalInfo,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quick Manual guides (Dialogs):
        if (showTypeGuide) {
            AlertDialog(
                onDismissRequest = { showTypeGuide = false },
                title = { Text("Account Type Guide") },
                text = {
                    Text(
                        "The DEALER Framework:\n\n" +
                                "• Drawings & Dividends: Withdrawals by owners.\n" +
                                "• Expenses: Operational costs (Rent, Salaries).\n" +
                                "• Assets: Resources owned (Cash, Equipment).\n" +
                                "• Liabilities: Debts owed (Loans, Payables).\n" +
                                "• Equity: Owner's capital & retained earnings.\n" +
                                "• Revenue: Income generated from sales."
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showTypeGuide = false }) { Text("Got it") }
                }
            )
        }

        if (showCategoryGuide) {
            AlertDialog(
                onDismissRequest = { showCategoryGuide = false },
                title = { Text("Category Guide") },
                text = {
                    Text(
                        "Categories break down types into specifics:\n\n" +
                                "• Current Asset: Liquid within 12 months (Cash).\n" +
                                "• Fixed Asset: Long-term (Machinery).\n" +
                                "• Current Liability: Due within 12 months.\n" +
                                "• Long-Term Liability: Due after 12 months.\n" +
                                "• Direct Expense: Tied directly to production (COGS).\n" +
                                "• Operating Expense: General business costs."
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showCategoryGuide = false }) { Text("Got it") }
                }
            )
        }
    }
}