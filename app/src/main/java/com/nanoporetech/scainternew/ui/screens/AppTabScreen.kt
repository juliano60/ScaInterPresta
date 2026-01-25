package com.nanoporetech.scainternew.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.screens.support.SupportScreen
import com.nanoporetech.scainternew.ui.screens.HealthCareScreen
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme
import com.nanoporetech.scainternew.ui.utils.NavigationType

/** Route comports the different screens/routes we can navigate to **/
enum class Route {
    Home,
    Support
}

private data class TabSpec(
    /** route is the destination route **/
    val route: String,
    /** label is the tab label **/
    val label: String,
    /** icon is the tab icon **/
    val icon: ImageVector
)

@Composable
fun AppTabScreen(
    navigationType: NavigationType,
    onLogout: () -> Unit,
    onBack: () -> Unit,
) {
    val tabs = listOf(
        TabSpec(
            route = Route.Home.name,
            label = stringResource(R.string.page_home),
            icon = Icons.Outlined.Home
        ),
        TabSpec(
            route = Route.Support.name,
            label = stringResource(R.string.page_support),
            icon = Icons.AutoMirrored.Outlined.HelpOutline
        )
    )
    
    // single controller for tabs
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: tabs.first().route
    val showPermanentDrawer = navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER

    fun onTabPressed(route: String) {
        navController.navigate(route) {
            // do not create a copy if at top of the stack
            launchSingleTop = true
            // restore previous state if visited befo
            restoreState = true
            // pop everything above start destination
            popUpTo(tabs.first().route) { saveState = true }
        }
    }

    if (showPermanentDrawer) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.surface,
                    drawerContentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.drawer_width))) {

                    NavigationDrawerContent(
                        selectedRoute = currentRoute,
                        onTabPressed = { route -> onTabPressed(route) },
                        navigationItemContentList = tabs,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.drawer_padding_content))
                    )
                }
            }
        ) {
            MainContent(
                navigationType = navigationType,
                currentRoute = currentRoute,
                navController = navController,
                tabs = tabs,
                onBack = onBack,
                onLogout = onLogout,
                onTabPressed = { route -> onTabPressed(route) },
            )
        }
    } else {
        MainContent(
            navigationType = navigationType,
            currentRoute = currentRoute,
            navController = navController,
            tabs = tabs,
            onBack = onBack,
            onLogout = onLogout,
            onTabPressed = { route -> onTabPressed(route) },
        )
    }
}

@Composable
private fun MainContent(
    navigationType: NavigationType,
    currentRoute: String,
    tabs: List<TabSpec>,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onTabPressed: (String) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val showBottomBar = navigationType == NavigationType.BOTTOM_NAVIGATION

    Scaffold(
        topBar = {
            AppTopBar(
                onLogout = onLogout
            )
        },
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    currentRoute = currentRoute,
                    tabs = tabs,
                    onTabPressed = onTabPressed
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = tabs.first().route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Route.Home.name) {
                HealthCareScreen(
                    modifier = Modifier
                        .background(color = AppConstants.lightGreen)
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(Route.Support.name) {
                SupportScreen(
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun AppBottomNavigationBar(
    currentRoute: String,
    tabs: List<TabSpec>,
    onTabPressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier) {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = { onTabPressed(tab.route) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}

@Composable
private fun NavigationDrawerContent(
    selectedRoute: String,
    onTabPressed: (String) -> Unit,
    navigationItemContentList: List<TabSpec>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        // HEADER
        DrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))

        // CONTENT
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(
                label = {
                    Text(
                        text = navItem.label
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                },
                onClick = { onTabPressed(navItem.route) },
                selected = selectedRoute == navItem.route,
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
            )
        }
    }
}

@Composable
private fun DrawerHeader(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = dimensionResource(R.dimen.padding_small))) {
        Image(
            painter = painterResource(R.drawable.sca_logo_no_title),
            contentDescription = stringResource(R.string.company_name),
            modifier = Modifier
                .size(32.dp)
        )
        Text(
            text = stringResource(R.string.company_name),
            style = MaterialTheme.typography.titleMedium
        )
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
        ),
        modifier = modifier
    )
}

/** Screens placeholders */
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
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                onBack = {},
                onLogout = {},
            )
        }
    }
}