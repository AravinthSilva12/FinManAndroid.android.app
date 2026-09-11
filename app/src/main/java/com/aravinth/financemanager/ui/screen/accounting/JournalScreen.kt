package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.aravinth.financemanager.domain.model.Accounting
import com.aravinth.financemanager.ui.navigation.Screen
import com.aravinth.financemanager.viewmodel.AccountingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class JournalFilter { ALL, TODAY, THIS_MONTH }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    navController: NavController,
    viewModel: AccountingViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
    var selectedFilter by remember { mutableStateOf(JournalFilter.ALL) }
    var entryToDelete by remember { mutableStateOf<Accounting?>(null) }

    // Filter Logic using Calendar
    val filteredTransactions = remember(transactions, selectedFilter) {
        val now = Calendar.getInstance()
        transactions.filter { entry ->
            val entryCal = Calendar.getInstance().apply { timeInMillis = entry.timestamp }
            when (selectedFilter) {
                JournalFilter.ALL -> true
                JournalFilter.TODAY -> {
                    now.get(Calendar.YEAR) == entryCal.get(Calendar.YEAR) &&
                            now.get(Calendar.DAY_OF_YEAR) == entryCal.get(Calendar.DAY_OF_YEAR)
                }
                JournalFilter.THIS_MONTH -> {
                    now.get(Calendar.YEAR) == entryCal.get(Calendar.YEAR) &&
                            now.get(Calendar.MONTH) == entryCal.get(Calendar.MONTH)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Journal Entries",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == JournalFilter.ALL,
                    onClick = { selectedFilter = JournalFilter.ALL },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = selectedFilter == JournalFilter.TODAY,
                    onClick = { selectedFilter = JournalFilter.TODAY },
                    label = { Text("Today") }
                )
                FilterChip(
                    selected = selectedFilter == JournalFilter.THIS_MONTH,
                    onClick = { selectedFilter = JournalFilter.THIS_MONTH },
                    label = { Text("This Month") }
                )
            }

            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (transactions.isEmpty()) "No journal entries recorded yet." else "No entries match this filter.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 0.dp,
                        bottom = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = filteredTransactions,
                        key = { entry -> entry.id }
                    ) { entry ->
                        JournalEntryCard(
                            entry = entry,
                            onDeleteClick = { entryToDelete = entry },
                            onCardClick = {
                                navController.navigate(Screen.EntryDetailScreen(transactionId = entry.id))
                            }
                        )
                    }
                }
            }

            // Delete Dialog
            entryToDelete?.let { entry ->
                AlertDialog(
                    onDismissRequest = { entryToDelete = null },
                    title = { Text("Delete this Journal Entry?") },
                    text = { Text("Are you sure you want to delete this entry? This action automatically updates your Ledger in real time.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.onDeleteTransaction(entry)
                                entryToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { entryToDelete = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun JournalEntryCard(
    entry: Accounting,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val formattedDate = remember(entry.timestamp) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(entry.timestamp))
    }

    val formattedAmount = remember(entry.amount) {
        "₹%.2f".format(entry.amount)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Category & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.category.name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Main Amount
            Text(
                text = formattedAmount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Section: Badges on left, Trash icon on bottom-right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AccountBadge(label = "Dr", isDebit = true)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = entry.debitAccount.ifEmpty { "Cash a/c" },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AccountBadge(label = "Cr", isDebit = false)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = entry.creditAccount.ifEmpty { "Bank a/c" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Trash icon placed strictly at bottom-right inside Row
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Entry",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountBadge(label: String, isDebit: Boolean) {
    val backgroundColor = if (isDebit) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor = if (isDebit) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}