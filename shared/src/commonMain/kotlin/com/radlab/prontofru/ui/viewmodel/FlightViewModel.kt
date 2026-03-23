package com.radlab.prontofru.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlab.prontofru.domain.repository.FlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlightViewModel(
    private val flightRepository: FlightRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FlightUiState>(FlightUiState.Success(emptyList()))
    val uiState: StateFlow<FlightUiState> = _uiState.asStateFlow()

    fun fetchFlights() {
        viewModelScope.launch {
            _uiState.value = FlightUiState.Loading
            try {
                val flights = flightRepository.getWeekendFlights(origin = "")
                _uiState.value = FlightUiState.Success(flights)
            } catch (e: Throwable) {
                _uiState.value = FlightUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
