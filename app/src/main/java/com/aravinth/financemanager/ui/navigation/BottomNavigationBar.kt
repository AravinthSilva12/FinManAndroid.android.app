package com.aravinth.financemanager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState


data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Any
)

@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, screen = Screen.Home),
        BottomNavItem("Assets", Icons.Default.Business, screen = Screen.Assets),
        BottomNavItem("Accounting", Icons.Default.AccountBalance, screen = Screen.Accounting),
        BottomNavItem("Budgeting", Icons.Default.Payments, screen = Screen.Budgeting)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar{
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.screen::class)
            } == true

            NavigationBarItem(
                selected = isSelected,
                label = { Text(text = item.label) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },

                onClick = {
                   navController.navigate(item.screen) {
                       popUpTo(navController.graph.findStartDestination().id) {
                           saveState = true
                       }

                       launchSingleTop = true

                       restoreState = true
                   }
                }
            )
        }
    }
}