package com.nanoporetech.scainternew.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.nanoporetech.scainternew.network.ScaApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface BarScanState {
    data object Idle : BarScanState
    data class ScanSuccess(val value: String) : BarScanState
    data class Error(val error: String) : BarScanState
    data object Loading : BarScanState
}

private const val TAG = "ConsultationViewModel"

class ConsultationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ConsultationUiState())
    val uiState: StateFlow<ConsultationUiState> = _uiState.asStateFlow()


    fun onBarcodeDetected(barcodes: List<Barcode>) {
        viewModelScope.launch {
            if (barcodes.isEmpty()) {
                updateScanState(BarScanState.Error("No barcode detected"))
                return@launch
            }

            updateScanState(BarScanState.Loading)

            barcodes.forEach { barcode ->
                barcode.rawValue?.let { barcodeValue ->
                    try {
                        val value = barcodeValue
                        updateScanState(BarScanState.ScanSuccess(value))
                    } catch (e: Exception) {
                        Log.i(TAG, "onBarcodeDetected: $e")
                        updateScanState(BarScanState.Error(e.message.toString()))
                    }
                    return@launch
                }
            }

            updateScanState(BarScanState.Error("No valid barcode value"))
        }
    }

    fun updateScanState(scanState: BarScanState) {
        _uiState.update { currentState ->
            currentState.copy(
                barScanState = scanState
            )
        }
    }

    fun fetchFamilyMembers() {
        viewModelScope.launch {
            try {
                val response = ScaApi.retrofitService.fetchFamilyMembers(
                    action="fetch_family",
                    familyId = "1825"
                )

                if (response.isSuccessful) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            familyMembers = response.body() ?: emptyList()
                        )
                    }
                }
                else {
                    when (response.code()) {
                        404 -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    familyMembers = emptyList()
                                )
                            }
                            //emitMessage(R.string.err_invalid_credentials)
                        }
                        else -> {
                            //emitMessage(R.string.err_unknown_error)
                            Log.e(TAG, "Error ${response.code()}")
                        }
                    }
                }

            } catch (e: IOException) {
                Log.d(TAG, e.toString())
            }
        }
    }
}