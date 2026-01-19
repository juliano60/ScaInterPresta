package com.nanoporetech.scainternew

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppTabScreen(
    isLoggedIn: Boolean,
    onShowLogin: @Composable () -> Unit = { DummyScreen() },
) {
    if (isLoggedIn) {
        LoggedInTabs()
    } else {
        onShowLogin()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoggedInTabs() {
    val navController = rememberNavController()

    // Bottom tabs
    val tabs = listOf(
        TabSpec(
            route = "home",
            label = stringResource(R.string.home),
            icon = Icons.Outlined.Home
        ),
        TabSpec(
            route = "support",
            label = stringResource(R.string.support_title),
            icon = Icons.AutoMirrored.Outlined.HelpOutline
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: tabs.first().route

    Scaffold(
        topBar = {
            AppTopBar()
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = tabs.first().route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeViewHealthCare() }
            composable("support") { SupportView() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier
) {
    LargeTopAppBar(
        title = { Text(stringResource(R.string.dummy_title)) },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

private data class TabSpec(
    val route: String,
    val label: String,
    val icon: ImageVector
)

/** Screens placeholders */
@Composable fun HomeViewHealthCare() { /* ... */ }
@Composable fun SupportView() { /* ... */ }
@Composable fun DummyScreen() { /* ... */ }