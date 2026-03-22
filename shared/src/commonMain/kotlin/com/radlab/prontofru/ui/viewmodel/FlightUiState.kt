package com.radlab.prontofru.ui.viewmodel

import com.radlab.prontofru.domain.model.Flight

sealed interface FlightUiState {
    data object Loading : FlightUiState
    data class Success(val flights: List<Flight>) : FlightUiState
    data class Error(val message: String) : FlightUiState
}
