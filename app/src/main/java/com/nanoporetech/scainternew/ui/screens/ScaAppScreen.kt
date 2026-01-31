package com.nanoporetech.scainternew.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainternew.model.AppViewModel
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.model.UiEvent
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.data.Datasource
import com.nanoporetech.scainternew.screens.consultation.ConsultationDetailScreen
import com.nanoporetech.scainternew.screens.examination.ExaminationListView
import com.nanoporetech.scainternew.screens.hospitalisation.HospitalisationListView
import com.nanoporetech.scainternew.screens.login.ForgottenPasswordScreen
import com.nanoporetech.scainternew.screens.login.LoginScreen
import com.nanoporetech.scainternew.ui.utils.NavigationType
import kotlinx.coroutines.flow.collectLatest

enum class Dest {
    Login,
    ConsultationList,
    NewConsultation,
    ExaminationList,
    HospitalisationList,
    ForgotPassword,
    TabsScreen,
    ConsultationDetailView,
}

@SuppressLint("RememberReturnType")
@Composable
fun App(
    windowSize: WindowWidthSizeClass,
    model: AppViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val uiState = model.uiState.collectAsState()
    val isLoggedIn = uiState.value.isLoggedIn
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val navigationType: NavigationType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER
        }
        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }
    }

    // handle snackbar events
    LaunchedEffect(Unit) {
        model.events.collectLatest { event ->
            when(event) {
                is UiEvent.Message -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.textId),
                        withDismissAction = true
                    )
                }
            }
        }
    }

    LaunchedEffect(isLoggedIn) {
        val dest = if (isLoggedIn) Dest.TabsScreen.name else Dest.Login.name
        navHostController.navigate(dest) {
            launchSingleTop = true
            popUpTo(navHostController.graph.id) { inclusive = true }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = if (isLoggedIn) Dest.TabsScreen.name else Dest.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Dest.Login.name) {
                LoginScreen(
                    newUsername = uiState.value.username,
                    newPassword = uiState.value.password,
                    onUsernameChanged = { model.updateUsername(it) },
                    onPasswordChanged = { model.updatePassword(it) },
                    onSubmit = {
                        model.checkCredentials()
                    },
                    onForgottenPassword = { navHostController.navigate(Dest.ForgotPassword.name) },
                    isLoginInvalid = uiState.value.isLoginInvalid,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppConstants.lightGreen)
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(Dest.ExaminationList.name) {
                ExaminationListView(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(Dest.HospitalisationList.name) {
                HospitalisationListView(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(Dest.ForgotPassword.name) {
                ForgottenPasswordScreen(
                    onBack = {
                        navHostController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(Dest.ConsultationDetailView.name) {
                ConsultationDetailScreen(
                    consultation = Datasource.consultations().first(),
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(Dest.TabsScreen.name) {
                AppTabScreen(
                    navigationType = navigationType,
                    onLogout = {
                        model.logout()
                    },
                )
            }
        }
    }
}