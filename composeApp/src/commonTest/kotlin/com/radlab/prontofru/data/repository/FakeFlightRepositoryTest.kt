package com.radlab.prontofru.data.repository

import com.radlab.prontofru.domain.model.Flight
import com.radlab.prontofru.domain.model.Money
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.time.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FakeFlightRepositoryTest {

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }

    private val fixedInstant = Instant.parse("2025-06-01T12:00:00Z")
    private val testTimeZone = TimeZone.UTC
    private val clock = FixedClock(fixedInstant)

    @Test
    fun getWeekendFlights_blankOrigin_usesWawAsDefault() = runTest {
        val repo = FakeFlightRepository(clock, testTimeZone)
        val deferred = async { repo.getWeekendFlights("") }
        advanceTimeBy(250)
        val flights = deferred.await()
        assertTrue(flights.isNotEmpty())
        assertEquals("WAW", flights.first().origin)
    }

    @Test
    fun getWeekendFlights_preservesOrigin() = runTest {
        val repo = FakeFlightRepository(clock, testTimeZone)
        val deferred = async { repo.getWeekendFlights("KRK") }
        advanceTimeBy(250)
        val flights = deferred.await()
        assertEquals(2, flights.size)
        assertTrue(flights.all { it.origin == "KRK" })
    }

    @Test
    fun getWeekendFlights_returnsExpectedShape() = runTest {
        val repo = FakeFlightRepository(clock, testTimeZone)
        val deferred = async { repo.getWeekendFlights("WAW") }
        advanceTimeBy(250)
        val flights = deferred.await()
        
        // 2025-06-01 is Sunday. 
        // Next Friday is 2025-06-06.
        // First weekend: Saturday 2025-06-07, Sunday 2025-06-08
        // Second weekend: Saturday 2025-06-14, Sunday 2025-06-15
        
        assertEquals(
            Flight(
                id = "fake-1",
                price = Money(29999, "PLN"),
                origin = "WAW",
                destination = "BCN",
                departureDate = LocalDateTime(2025, 6, 7, 8, 30),
                returnDate = LocalDateTime(2025, 6, 8, 21, 45),
            ),
            flights[0],
        )
        
        assertEquals(
            Flight(
                id = "fake-2",
                price = Money(18950, "PLN"),
                origin = "WAW",
                destination = "LIS",
                departureDate = LocalDateTime(2025, 6, 14, 6, 15),
                returnDate = LocalDateTime(2025, 6, 15, 19, 0),
            ),
            flights[1],
        )
    }
}
