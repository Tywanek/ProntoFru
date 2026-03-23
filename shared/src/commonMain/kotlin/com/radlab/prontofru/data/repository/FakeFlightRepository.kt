package com.radlab.prontofru.data.repository

import com.radlab.prontofru.domain.model.Flight
import com.radlab.prontofru.domain.model.Money
import com.radlab.prontofru.domain.repository.FlightRepository
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class FakeFlightRepository(
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()
) : FlightRepository {

    override suspend fun getWeekendFlights(origin: String): List<Flight> {
        delay(250)
        val now = clock.now().toLocalDateTime(timeZone)
        val from = origin.ifBlank { "WAW" }

        // Find next Friday
        val firstFriday = findNextDayOfWeek(now, DayOfWeek.FRIDAY)
        val firstSaturday = firstFriday.date.plus(1, DateTimeUnit.DAY)
        val firstSunday = firstFriday.date.plus(2, DateTimeUnit.DAY)

        // Find Friday of the next week
        val secondFriday = firstFriday.date.plus(7, DateTimeUnit.DAY)
        val secondSaturday = secondFriday.plus(1, DateTimeUnit.DAY)
        val secondSunday = secondFriday.plus(2, DateTimeUnit.DAY)

        return listOf(
            Flight(
                id = "fake-1",
                price = Money(29999, "PLN"),
                origin = from,
                destination = "BCN",
                departureDate = LocalDateTime(firstSaturday.year, firstSaturday.month, firstSaturday.dayOfMonth, 8, 30),
                returnDate = LocalDateTime(firstSunday.year, firstSunday.month, firstSunday.dayOfMonth, 21, 45),
            ),
            Flight(
                id = "fake-2",
                price = Money(18950, "PLN"),
                origin = from,
                destination = "LIS",
                departureDate = LocalDateTime(secondSaturday.year, secondSaturday.month, secondSaturday.dayOfMonth, 6, 15),
                returnDate = LocalDateTime(secondSunday.year, secondSunday.month, secondSunday.dayOfMonth, 19, 0),
            ),
        )
    }

    private fun findNextDayOfWeek(from: LocalDateTime, dayOfWeek: DayOfWeek): LocalDateTime {
        var current = from.date
        if (current.dayOfWeek == dayOfWeek) {
             current = current.plus(1, DateTimeUnit.DAY)
        }
        while (current.dayOfWeek != dayOfWeek) {
            current = current.plus(1, DateTimeUnit.DAY)
        }
        return LocalDateTime(current.year, current.month, current.dayOfMonth, 0, 0)
    }
}
