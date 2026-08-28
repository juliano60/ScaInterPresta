package com.nanoporetech.scainter.ui.hospitalisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.HospitalisationUiState
import com.nanoporetech.scainter.data.NewRegularHospitalisationResult
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.ui.events.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegularHospitalisationViewModel(
    private val providerName: String,
    private val userId: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(HospitalisationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiMessage>()
    val events = _events.asSharedFlow()

    fun submitRequest() {
        if (_uiState.value.isSubmitting) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSubmitting = true)
            }

            try {
                when (repository.newRegularHospitalisation(
                    provider = providerName,
                    userId = userId,
                    type = _uiState.value.selectedHospitalisationType,
                    reason = _uiState.value.reason,
                    numDays = _uiState.value.numDays,
                    roomType = _uiState.value.selectedRoomType,
                    cost = _uiState.value.roomCost,
                )) {
                    NewRegularHospitalisationResult.Success -> {
                        _events.emit(UiMessage.Success(R.string.new_regular_hosp_success_message))
                    }

                    NewRegularHospitalisationResult.NetworkError -> {
                        _events.emit(UiMessage.Error(R.string.err_network_error_message))
                    }

                    NewRegularHospitalisationResult.UnknownError -> {
                        _events.emit(UiMessage.Error(R.string.err_unknown_error_message))
                    }
                }
            } finally {
                _uiState.update {
                    it.copy(isSubmitting = false)
                }
            }
        }
    }

    fun setHospitalisationType(value: String) {
        _uiState.update {
            it.copy(
                selectedHospitalisationType = value
            )
        }
    }

    fun setReason(value: String) {
        _uiState.update {
            it.copy(
                reason = value
            )
        }
    }

    fun setNumDays(value: String) {
        _uiState.update {
            it.copy(
                numDays = value
            )
        }
    }

    fun setRoomType(value: String) {
        _uiState.update {
            it.copy(
                selectedRoomType = value
            )
        }
    }

    fun setRoomCost(value: String) {
        _uiState.update {
            it.copy(
                roomCost = value
            )
        }
    }

    companion object {
        fun provideFactory(
            providerName: String,
            //insuranceType: String,
            userId: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                RegularHospitalisationViewModel(
                    providerName = providerName,
                    //insuranceType = insuranceType,
                    userId = userId,
                    repository = repository
                )
            }
        }
    }
}