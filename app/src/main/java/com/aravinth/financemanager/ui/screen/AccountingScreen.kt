package com.aravinth.financemanager.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AccountingScreen(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize().padding())
    {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            Text(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text =  "Accounting Screen"
            )
        }
    }
}