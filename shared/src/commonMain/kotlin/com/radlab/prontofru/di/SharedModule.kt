package com.radlab.prontofru.di

import com.radlab.prontofru.data.repository.FakeFlightRepository
import com.radlab.prontofru.domain.repository.FlightRepository
import com.radlab.prontofru.ui.viewmodel.FlightViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<FlightRepository> { FakeFlightRepository() }
    viewModelOf(::FlightViewModel)
}
