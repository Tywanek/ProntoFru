package com.radlab.prontofru.domain.model

data class Money(
    val amountInMinorUnits: Long,
    val currency: String
) {
    fun format(): String {
        val major = amountInMinorUnits / 100
        val minor = amountInMinorUnits % 100
        return "$major.${minor.toString().padStart(2, '0')} $currency"
    }
}
