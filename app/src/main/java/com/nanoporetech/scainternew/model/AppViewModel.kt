package com.nanoporetech.scainternew.model

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.network.Credentials
import com.nanoporetech.scainternew.network.ScaApi
import com.nanoporetech.scainternew.network.FetchProviderRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

//sealed interface
private const val TAG = "AppViewModel"

sealed interface UiEvent {
    data class Message(
       @StringRes val textId: Int
    ): UiEvent
}

class AppViewModel: ViewModel() {
    // app state
    private var _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun login(credentials: Credentials) {
        viewModelScope.launch {
            try {
                val request = FetchProviderRequest(
                    action = "fetch",
                    username = credentials.username,
                    password = credentials.password
                )
                val response = ScaApi.retrofitService.loginProvider(request)

                if (response.isSuccessful) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoggedIn = true
                        )
                    }
                }
                else {
                    when (response.code()) {
                        404 -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoggedIn = false,
                                    isLoginInvalid = true,
                                )
                            }
                            emitMessage(R.string.err_invalid_credentials)
                        }
                        else -> {
                            emitMessage(R.string.err_unknown_error)
                        }
                    }
                }
            } catch (e: IOException) {
                Log.d(TAG, e.toString())
                emitMessage(R.string.err_connection_offline)
            }
        }
    }

    private fun emitMessage(messageId: Int) {
        viewModelScope.launch {
            _events.emit(UiEvent.Message(messageId))
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

        // now attempt to login
        login(credentials)
    }
}