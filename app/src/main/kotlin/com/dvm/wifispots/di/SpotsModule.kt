package com.dvm.wifispots.di

import com.dvm.wifispots.data.DefaultSpotsRepository
import com.dvm.wifispots.data.SpotsRepository
import com.dvm.wifispots.presentation.SpotsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val spotsModule = module {

    single<SpotsRepository> {
        DefaultSpotsRepository(get())
    }

    viewModel { SpotsViewModel(get()) }
}