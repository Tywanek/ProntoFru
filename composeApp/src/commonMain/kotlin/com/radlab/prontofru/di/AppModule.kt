package com.radlab.prontofru.di

import com.radlab.prontofru.data.repository.FakeFlightRepository
import com.radlab.prontofru.domain.repository.FlightRepository
import com.radlab.prontofru.ui.viewmodel.FlightViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<FlightRepository> { FakeFlightRepository() }
    viewModelOf(::FlightViewModel)
}
