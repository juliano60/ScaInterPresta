package com.nanoporetech.scainternew

import android.content.Context
import androidx.lifecycle.ViewModel
import com.nanoporetech.scainternew.api.BackendApi
import com.nanoporetech.scainternew.api.RetrofitInstance
import com.nanoporetech.scainternew.model.AppUiState
import com.nanoporetech.scainternew.model.FetchProviderRequest
import com.nanoporetech.scainternew.model.Provider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class AppViewModel: ViewModel() {
    // app state
    private var _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun login(
        ctx: Context
    ) {

        // create retrofit builder
        val retrofit = RetrofitInstance.getRetrofitInstance()

        // create retrofit instance api
        val backendApi = retrofit.create(BackendApi::class.java)

        val request = FetchProviderRequest(
            action = "fetch",
            username = _uiState.value.username,
            password = _uiState.value.password
        )

        //val call: Response<Provider> = backendApi.fetchProvider(request)
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
}