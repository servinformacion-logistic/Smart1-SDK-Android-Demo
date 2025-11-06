package com.servinformacion.smart1sdkdemo.di

import com.servinformacion.smart1sdkdemo.home.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelDiModule = module {
    factoryOf(::HomeViewModel)
}