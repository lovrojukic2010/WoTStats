package com.example.wotstats.di

import com.example.wotstats.viewmodel.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SignInViewModel> { SignInViewModel() }
}