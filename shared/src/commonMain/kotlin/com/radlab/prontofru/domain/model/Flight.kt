package com.radlab.prontofru.domain.model

import kotlinx.datetime.LocalDateTime

data class Flight(
    val id: String,
    val price: Money,
    val origin: String,
    val destination: String,
    val departureDate: LocalDateTime,
    val returnDate: LocalDateTime,
)
