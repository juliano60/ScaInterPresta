package com.nanoporetech.scainternew

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainternew.model.LoginViewModel
import com.nanoporetech.scainternew.presentation.LoginScreen
import com.nanoporetech.scainternew.presentation.consultation.ConsultationListView
import com.nanoporetech.scainternew.presentation.examination.ExaminationListView
import com.nanoporetech.scainternew.presentation.hospitalisation.HospitalisationListView

enum class Dest {
    Login,
    ConsultationList,
    ExaminationList,
    HospitalisationList,
    ForgotPassword,
    Tabs
}

@Composable
fun BlankScreen() {}

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavRoot(loginModel = LoginViewModel())
        }
    }
}

@Composable
fun AppNavRoot(loginModel: LoginViewModel) {
    val nav = rememberNavController()
    //val isLoggedIn by loginModel.isLoggedIn.collectAsState()
    val isLoggedIn = true

    val target = if (isLoggedIn) Dest.ConsultationList.name else Dest.Login.name
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    LaunchedEffect(target, currentRoute) {
        if (currentRoute != null && currentRoute != target) {
            nav.navigate(target) {
                launchSingleTop = true
                popUpTo(nav.graph.id) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = nav,
        startDestination = if (isLoggedIn) Dest.Tabs.name else Dest.Login.name
    ) {
        composable(Dest.Login.name) {
            LoginScreen(
                navController = nav,
                model = viewModel()
            )
        }
        composable(Dest.ConsultationList.name) {
            ConsultationListView(
                navController = nav,
                //model = viewModel()
            )
        }
        composable(Dest.ExaminationList.name) {
            ExaminationListView(
                navController = nav,
                //model = viewModel()
            )
        }
        composable(Dest.HospitalisationList.name) {
            HospitalisationListView(
                navController = nav,
                //model = viewModel()
            )
        }
        composable(Dest.Tabs.name) {
            BlankScreen()
            /*AppTabs(
                loginModel = loginModel
            )*/
        }
    }
}