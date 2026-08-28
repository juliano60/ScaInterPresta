package com.nanoporetech.scainter.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.events.UiMessage
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationDetailsScreen
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationFamilyMembersListScreen
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationListScreen
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationPolicyHolderDetailsScreen
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationRegularHospScreen
import com.nanoporetech.scainter.ui.hospitalisation.ListHospitalisationsViewModel
import com.nanoporetech.scainter.ui.hospitalisation.NewHospitalisationScreen
import com.nanoporetech.scainter.ui.hospitalisation.NewHospitalisationViewModel
import com.nanoporetech.scainter.ui.hospitalisation.RegularHospitalisationViewModel
import com.nanoporetech.scainter.ui.menu.NavGraphs
import com.nanoporetech.scainter.ui.menu.NavResult
import com.nanoporetech.scainter.ui.menu.ScaAppScreen
import com.nanoporetech.scainter.ui.utils.AppSnackbarVisuals
import com.nanoporetech.scainter.ui.utils.SnackbarType


private const val TAG = "HospitalisationNavigation"
private const val NAV_RESULT = "nav_result"

object HospitalisationPolicyHolderDetails {
    const val POLICY_HOLDER_ID = "policyHolderId"

    val route =
        "${ScaAppScreen.HospitalisationPolicyHolderDetails.name}/{$POLICY_HOLDER_ID}"

    fun createRoute(policyHolderId: Int) =
        "${ScaAppScreen.HospitalisationPolicyHolderDetails.name}/$policyHolderId"
}

object HospitalisationDetails {
    const val HOSPITALISATION_ID = "hospitalisationId"

    val route =
        "${ScaAppScreen.HospitalisationDetails.name}/{$HOSPITALISATION_ID}"

    fun createRoute(hospitalisationId: Int) =
        "${ScaAppScreen.HospitalisationDetails.name}/$hospitalisationId"
}

fun NavGraphBuilder.hospitalisationNavigation(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState
) {
    navigation(
        route = NavGraphs.NEW_HOSPITALISATION,
        startDestination = ScaAppScreen.HospitalisationNewHospitalisation.name
    ) {
        newHospitalisationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }

    navigation(
        route = NavGraphs.EXISTING_HOSPITALISATION,
        startDestination = ScaAppScreen.HospitalisationList.name
    ) {
        existingHospitalisationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.newHospitalisationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState,
) {
    composable(route = ScaAppScreen.HospitalisationNewHospitalisation.name) { backStackEntry ->
        val scanResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(SCAN_RESULT, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(scanResult) {
            scanResult?.let { familyId ->
                val parentEntry = navController.getBackStackEntry(
                    NavGraphs.NEW_HOSPITALISATION
                )
                parentEntry.savedStateHandle[FAMILY_ID] = familyId
                backStackEntry.savedStateHandle[SCAN_RESULT] = null
                navController.navigate(
                    route = ScaAppScreen.HospitalisationFamilyMembersList.name
                )
            }
        }

        NewHospitalisationScreen(
            onScanQrCode = {
                navController.navigate(
                    route = ScaAppScreen.CodeScanner.name
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium)),
        )
    }

    composable(route = ScaAppScreen.HospitalisationFamilyMembersList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_HOSPITALISATION)
        }

        val viewModel = newHospitalisationViewModel(
            navController,
            backStackEntry,
            providerName
        )

        val scanResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(SCAN_RESULT, null)
            .collectAsStateWithLifecycle()

        val familyId by parentEntry.savedStateHandle
            .getStateFlow<String?>(FAMILY_ID, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(scanResult) {
            scanResult?.let { newFamilyId ->
                parentEntry.savedStateHandle[FAMILY_ID] = newFamilyId
                backStackEntry.savedStateHandle[SCAN_RESULT] = null
            }
        }

        LaunchedEffect(familyId) {
            familyId?.let(viewModel::loadFamily)
        }

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        HospitalisationFamilyMembersListScreen(
            members = state.familyMembers,
            isLoading = state.isLoading,
            onMemberSelected = { policyHolderId ->
                navController.navigate(route = HospitalisationPolicyHolderDetails.createRoute(policyHolderId))
            },
            onScanQrCode = {
                navController.navigate(
                    route = ScaAppScreen.CodeScanner
                )
            },
            modifier = Modifier
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }

    composable(
        route = HospitalisationPolicyHolderDetails.route,
        arguments = listOf(
            navArgument(HospitalisationPolicyHolderDetails.POLICY_HOLDER_ID) {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val policyHolderId = requireNotNull(
            backStackEntry.arguments?.getInt(
                ExaminationPolicyHolderDetails.POLICY_HOLDER_ID
            )
        )

        val viewModel = newHospitalisationViewModel(
            navController,
            backStackEntry,
            providerName
        )

        val localUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = localUiState.policyHolders.firstOrNull { it.id == policyHolderId }

        Log.d(TAG, "PolicyHolder: $policyHolder")

        // NOTE: do the setPolicyHolder in a LaunchedEffect to avoid repeated recompositions
        LaunchedEffect(policyHolder) {
            policyHolder?.let(viewModel::setPolicyHolder)
        }

        if (policyHolder != null) {
            HospitalisationPolicyHolderDetailsScreen(
                policyHolder = policyHolder,
                onNewHospitalisation = {
                    navController.navigate(ScaAppScreen.HospitalisationRegularHospitalisation.name)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }

    composable(route = ScaAppScreen.HospitalisationRegularHospitalisation.name) { backStackEntry ->
        val newExaminationViewModel = newHospitalisationViewModel(
            navController,
            backStackEntry,
            providerName
        )

        val newHospitalisationState by newExaminationViewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = newHospitalisationState.currentPolicyHolder

        if (policyHolder != null) {
            val viewModel: RegularHospitalisationViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                key = "regular_hosp_${policyHolder.id}",
                factory = RegularHospitalisationViewModel.provideFactory(
                    providerName = providerName,
                    userId = policyHolder.id.toString()
                )
            )

            val context = LocalContext.current
            val localUiState by viewModel.uiState.collectAsStateWithLifecycle()

            // convert from model.events to nav_result
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiMessage.Success -> {
                            val dashboardEntry = navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)

                            dashboardEntry.savedStateHandle[NAV_RESULT] = NavResult.NewRegularHospitalisationSuccess.name

                            navController.navigate(NavGraphs.EXISTING_HOSPITALISATION) {
                                popUpTo(ScaAppScreen.HealthCareDashboard.name) {
                                    inclusive = false
                                }
                            }
                        }
                        is UiMessage.Error -> {
                            snackbarHostState.showSnackbar(
                                AppSnackbarVisuals(
                                    message = context.getString(event.errorId),
                                    type = SnackbarType.Error,
                                    duration = SnackbarDuration.Long
                                )
                            )
                        }
                    }
                }
            }

            HospitalisationRegularHospScreen(
                state = localUiState,
                onHospitalisationTypeSelected = viewModel::setHospitalisationType,
                onReasonChanged = viewModel::setReason,
                onNumDaysChanged = viewModel::setNumDays,
                onRoomTypeSelected = viewModel::setRoomType,
                onRoomCostChanged = viewModel::setRoomCost,
                onSubmitRequest = viewModel::submitRequest,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium)),
            )
        }
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.existingHospitalisationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState) {

    composable(route = ScaAppScreen.HospitalisationList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.EXISTING_HOSPITALISATION)
        }

        val dashboardEntry = remember(backStackEntry) {
            navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)
        }
        val dashboardNavResult by dashboardEntry
            .savedStateHandle
            .getStateFlow<String?>(NAV_RESULT, null)
            .collectAsStateWithLifecycle()

        val context = LocalContext.current

        LaunchedEffect(dashboardNavResult) {
            when (dashboardNavResult) {
                NavResult.NewRegularHospitalisationSuccess.name -> {
                    snackbarHostState.showSnackbar(
                        AppSnackbarVisuals(
                            message = context.getString(R.string.new_regular_hosp_success_message),
                            type = SnackbarType.Success
                        )
                    )
                }
                null -> Unit
            }
            // now clear old nav result
            if (dashboardNavResult != null) {
                dashboardEntry.savedStateHandle[NAV_RESULT] = null
            }
        }

        val hospitalisationViewModel: ListHospitalisationsViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = ListHospitalisationsViewModel.provideFactory(
                providerName = providerName
            )
        )

        LaunchedEffect(Unit) {
            hospitalisationViewModel.loadHospitalisations()
        }

        val uiState by hospitalisationViewModel.uiState.collectAsStateWithLifecycle()

        HospitalisationListScreen(
            hospitalisations = uiState.hospitalisations,
            isLoading = uiState.isLoading,
            onRowClick = { examination ->
                navController.navigate(
                    route = HospitalisationDetails.createRoute(examination.id)
                )
            },
            onRefresh = hospitalisationViewModel::loadHospitalisations,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }

    composable(
        route = HospitalisationDetails.route,
        arguments = listOf(
            navArgument(HospitalisationDetails.HOSPITALISATION_ID) {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val hospitalisationId = requireNotNull(
            backStackEntry.arguments?.getInt(HospitalisationDetails.HOSPITALISATION_ID)
        )

        // grab parent's view model
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(
                NavGraphs.EXISTING_HOSPITALISATION
            )
        }

        val viewModel: ListHospitalisationsViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = ListHospitalisationsViewModel.provideFactory(
                providerName = providerName
            )
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        // extract hospitalisation with matching consultationId
        val hospitalisation = uiState.hospitalisations.find {
            it.id == hospitalisationId
        }

        if (hospitalisation != null) {
            HospitalisationDetailsScreen(
                hospitalisation = hospitalisation,
                isRefreshing = uiState.isLoading,
                onRefresh = viewModel::loadHospitalisations,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
private fun newHospitalisationViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    providerName: String
): NewHospitalisationViewModel {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry(NavGraphs.NEW_HOSPITALISATION)
    }

    return viewModel(
        viewModelStoreOwner = parentEntry,
        factory = NewHospitalisationViewModel.provideFactory(
            providerName = providerName
        )
    )
}