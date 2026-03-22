package com.radlab.prontofru.data.repository

import com.radlab.prontofru.domain.model.Flight
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeFlightRepositoryTest {

    @Test
    fun getWeekendFlights_blankOrigin_usesWawAsDefault() = runTest {
        val repo = FakeFlightRepository()
        val deferred = async { repo.getWeekendFlights("") }
        advanceTimeBy(250)
        val flights = deferred.await()
        assertTrue(flights.isNotEmpty())
        assertEquals("WAW", flights.first().origin)
    }

    @Test
    fun getWeekendFlights_preservesOrigin() = runTest {
        val repo = FakeFlightRepository()
        val deferred = async { repo.getWeekendFlights("KRK") }
        advanceTimeBy(250)
        val flights = deferred.await()
        assertEquals(2, flights.size)
        assertTrue(flights.all { it.origin == "KRK" })
    }

    @Test
    fun getWeekendFlights_returnsExpectedShape() = runTest {
        val repo = FakeFlightRepository()
        val deferred = async { repo.getWeekendFlights("WAW") }
        advanceTimeBy(250)
        val flights = deferred.await()
        assertEquals(
            Flight(
                id = "fake-1",
                price = 299.99,
                origin = "WAW",
                destination = "BCN",
                departureDate = LocalDateTime(2025, 6, 7, 8, 30),
                returnDate = LocalDateTime(2025, 6, 8, 21, 45),
            ),
            flights[0],
        )
        assertEquals("LIS", flights[1].destination)
    }
}
