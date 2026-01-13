package com.nanoporetech.scainternew

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.presentation.consultation.ConsultationListView
import com.nanoporetech.scainternew.presentation.examination.ExaminationListView
import com.nanoporetech.scainternew.presentation.hospitalisation.HospitalisationListView
import com.nanoporetech.scainternew.presentation.login.LoginScreen

enum class Dest {
    Login,
    ConsultationList,
    ExaminationList,
    HospitalisationList,
    ForgotPassword,
    Tabs
}

@Composable
fun App(
    model: AppViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val uiState = model.uiState.collectAsState()

    val isLoggedIn = uiState.value.isLoggedIn
    val target = if (isLoggedIn) Dest.ConsultationList.name else Dest.Login.name
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    LaunchedEffect(target, currentRoute) {
        if (currentRoute != null && currentRoute != target) {
            navHostController.navigate(target) {
                launchSingleTop = true
                popUpTo(navHostController.graph.id) { inclusive = true }
            }
        }
    }

    Scaffold(

    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = if (isLoggedIn) Dest.Tabs.name else Dest.Login.name
        ) {
            composable(Dest.Login.name) {
                LoginScreen(
                    newUsername = uiState.value.username,
                    newPassword = uiState.value.password,
                    onUsernameChanged = { model.updateUsername(it) },
                    onPasswordChanged = { model.updatePassword(it) },
                    onLogin = {
                        //model.login()
                    },
                    modifier = Modifier
                        .background(AppConstants.lightGreen)
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(Dest.ConsultationList.name) {
                ConsultationListView(
                )
            }
            composable(Dest.ExaminationList.name) {
                ExaminationListView(
                )
            }
            composable(Dest.HospitalisationList.name) {
                HospitalisationListView(
                )
            }
        }
    }
}