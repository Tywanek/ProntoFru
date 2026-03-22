package com.radlab.prontofru.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

class AppModuleKoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun appModule_definitionsAreResolvable() {
        sharedModule.verify()
    }
}
