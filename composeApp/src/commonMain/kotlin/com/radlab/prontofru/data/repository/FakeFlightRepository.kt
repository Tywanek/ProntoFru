package com.radlab.prontofru.data.repository

import com.radlab.prontofru.domain.model.Flight
import com.radlab.prontofru.domain.repository.FlightRepository
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime

class FakeFlightRepository : FlightRepository {

    override suspend fun getWeekendFlights(origin: String): List<Flight> {
        delay(250)
        val from = origin.ifBlank { "WAW" }
        return listOf(
            Flight(
                id = "fake-1",
                price = 299.99,
                origin = from,
                destination = "BCN",
                departureDate = LocalDateTime(2025, 6, 7, 8, 30),
                returnDate = LocalDateTime(2025, 6, 8, 21, 45),
            ),
            Flight(
                id = "fake-2",
                price = 189.50,
                origin = from,
                destination = "LIS",
                departureDate = LocalDateTime(2025, 6, 14, 6, 15),
                returnDate = LocalDateTime(2025, 6, 15, 19, 0),
            ),
        )
    }
}
