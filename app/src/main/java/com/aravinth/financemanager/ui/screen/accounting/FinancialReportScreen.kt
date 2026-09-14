package com.aravinth.financemanager.ui.screen.accounting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aravinth.financemanager.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialReportScreen(navController: NavController){
   Scaffold(
       topBar = {TopAppBar(
           title = { Text("Financial Reports", fontWeight = FontWeight.Bold)},
           navigationIcon = {IconButton(onClick = {navController.popBackStack()}){
               Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
         }
      } )
   }
   ) {innerPadding ->
       Column(
           modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
           verticalArrangement = Arrangement.spacedBy(12.dp)
       ){
           //1.Trial Balance:
           Button(onClick = {navController.navigate(Screen.TrialBalance)},
               modifier = Modifier.fillMaxWidth().height(56.dp)){
               Text("Trial Balance", style = MaterialTheme.typography.titleMedium)
           }

           //2.Income Statement:
           Button(
               onClick = {navController.navigate(Screen.IncomeStatement)},
               modifier = Modifier.fillMaxWidth().height(56.dp)
           ){
               Text("Income Statement", style = MaterialTheme.typography.titleMedium)
           }

           //3.Balance Sheet (pending*):
           OutlinedButton(
               onClick = { /* TODO: Balance Sheet */},
               modifier = Modifier.fillMaxWidth().height(56.dp),
               enabled = false
           ){
               Text("Balance Sheet", style = MaterialTheme.typography.titleMedium)
           }
       }
   }
}