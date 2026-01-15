package com.nanoporetech.scainternew

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanoporetech.scainternew.network.Credentials
import com.nanoporetech.scainternew.network.ScaApi
import com.nanoporetech.scainternew.model.AppUiState
import com.nanoporetech.scainternew.model.FetchProviderRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

//sealed interface
private const val TAG = "AppViewModel"

class AppViewModel: ViewModel() {
    // app state
    private var _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun login(credentials: Credentials) {
        viewModelScope.launch {
            try {
                val request = FetchProviderRequest(
                    action = "fetch",
                    username = credentials.username,
                    password = credentials.password
                )
                val provider = ScaApi.retrofitService.loginProvider(request)

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoggedIn = true
                    )
                }

            } catch (e: IOException) {
                Log.d(TAG, e.toString())
            }
        }
    }

    fun logout() {}

    fun updateUsername(value: String) {
        _uiState.update {  currentState ->
            currentState.copy(username = value)
        }
    }

    fun updatePassword(value: String) {
        _uiState.update {  currentState ->
            currentState.copy(password = value)
        }
    }

    fun checkCredentials() {
        val credentials = Credentials(
            username = _uiState.value.username,
            password = _uiState.value.password
        )

        if (credentials.isEmpty()) {
            // TODO: set error state
            return
        }

        // now attempt to login
        login(credentials)
    }
}