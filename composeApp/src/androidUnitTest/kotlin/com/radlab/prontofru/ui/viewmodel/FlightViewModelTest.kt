package com.radlab.prontofru.ui.viewmodel

import com.radlab.prontofru.domain.model.Flight
import com.radlab.prontofru.domain.model.Money
import com.radlab.prontofru.domain.repository.FlightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class FlightViewModelTest {

    @Test
    fun initialState_isSuccessWithEmptyList() = runTest {
        val mainDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(mainDispatcher)
        try {
            val vm = FlightViewModel(FakeRepo())
            val state = vm.uiState.value
            assertIs<FlightUiState.Success>(state)
            assertEquals(0, state.flights.size)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun fetchFlights_emitsLoadingThenSuccess() = runTest {
        val mainDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(mainDispatcher)
        try {
            val vm = FlightViewModel(DelayedRepo())
            vm.fetchFlights()
            
            // Run until the first suspension point (the delay in the repo)
            runCurrent()
            assertIs<FlightUiState.Loading>(vm.uiState.value)
            
            // Advance until everything is finished
            advanceUntilIdle()
            val success = vm.uiState.value
            assertIs<FlightUiState.Success>(success)
            assertEquals(1, success.flights.size)
            assertEquals("test-1", success.flights.first().id)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun fetchFlights_onRepositoryError_emitsError() = runTest {
        val mainDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(mainDispatcher)
        try {
            val vm = FlightViewModel(ThrowingRepo())
            vm.fetchFlights()
            advanceUntilIdle()
            val err = vm.uiState.value
            assertIs<FlightUiState.Error>(err)
            assertEquals("network down", err.message)
        } finally {
            Dispatchers.resetMain()
        }
    }

    private class FakeRepo : FlightRepository {
        override suspend fun getWeekendFlights(origin: String): List<Flight> =
            listOf(sampleFlight(origin))
    }

    private class DelayedRepo : FlightRepository {
        override suspend fun getWeekendFlights(origin: String): List<Flight> {
            delay(1)
            return listOf(sampleFlight(origin))
        }
    }

    private class ThrowingRepo : FlightRepository {
        override suspend fun getWeekendFlights(origin: String): List<Flight> {
            throw IllegalStateException("network down")
        }
    }
}

private fun sampleFlight(origin: String) = Flight(
    id = "test-1",
    price = Money(10000, "PLN"),
    origin = origin,
    destination = "XXX",
    departureDate = LocalDateTime(2025, 1, 1, 12, 0),
    returnDate = LocalDateTime(2025, 1, 2, 12, 0),
)
