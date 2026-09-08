package com.aravinth.financemanager.ui.screen.accounting

import android.R.attr.onClick
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableAccountDropdown(
     accountsList: List<String>,
     selectedAccount: String,
     searchQuery: String,
     onAccountSelectChange: (String) -> Unit,
     onSearchQueryChange: (String) -> Unit,
     onAddNewAccountClick: () -> Unit,
     label: String,
     modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
    val filteredAccounts = accountsList.filter {
        it.contains(searchQuery, ignoreCase = true) }

    ExposedDropdownMenuBox(expanded = expanded,
        onExpandedChange = {expanded = it})
    {
        OutlinedTextField(
            value = selectedAccount,
            onValueChange = {},
            readOnly = true,
            label = {Text(label)},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth().menuAnchor()
            )

        ExposedDropdownMenu(expanded = expanded,
            onDismissRequest = {expanded = false})
        {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = {Text("Search account..")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )

            DropdownMenuItem(
                text = {Text("+Add account")},
                onClick = {expanded = false
                onAddNewAccountClick()}
            )

           filteredAccounts.forEach{accountName ->
               DropdownMenuItem(
                   text = {Text(text = accountName)},
                   onClick = {
                       onAccountSelectChange(accountName)
                       expanded = false
                       onSearchQueryChange("")
                   }
               )
           }
        }
    }
}
