package com.radlab.prontofru.domain.repository

import com.radlab.prontofru.domain.model.Flight

interface FlightRepository {
    suspend fun getWeekendFlights(origin: String): List<Flight>
}
