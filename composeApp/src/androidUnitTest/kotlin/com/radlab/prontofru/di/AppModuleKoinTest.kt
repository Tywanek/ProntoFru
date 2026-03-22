package com.radlab.prontofru.di

import org.koin.test.verify.verify
import kotlin.test.Test

class AppModuleKoinTest {

    @Test
    fun appModule_definitionsAreResolvable() {
        appModule.verify()
    }
}
