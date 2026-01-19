package com.nanoporetech.scainternew

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainternew.screens.support.SupportScreen
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme

@Composable
fun AppTabScreen(
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    onShowLogin: @Composable () -> Unit = { DummyScreen() },
) {
    if (isLoggedIn) {
        LoggedInTabs(
            onBack = onBack,
            onLogout = onLogout
        )
    } else {
        onShowLogin()
    }
}

@Composable
private fun LoggedInTabs(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
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
            AppTopBar(
                onLogout = onLogout
            )
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
            composable("support") { SupportScreen(
                onBack = onBack
            ) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(R.string.health_care)) },
        actions = {
            IconButton(onClick = { onLogout() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = stringResource(R.string.logout)
                )
            }
        },
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
@Composable fun DummyScreen() { /* ... */ }

@Preview
@Composable
fun AppTabScreenPreview() {
    ScaInterNewTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppTabScreen(
                isLoggedIn = true,
                onBack = {},
                onLogout = {},
            )
        }
    }
}