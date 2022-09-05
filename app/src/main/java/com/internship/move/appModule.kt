package com.internship.move

import com.internship.move.feature.authentication.AuthenticationService
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.internship.move.model.InternalStorageManager
import com.internship.move.model.Repository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel { OnboardingViewModel(repo = get()) }
    viewModel { AuthenticationViewModel() }
}

val repository = module {
    single<Repository> { Repository(internalStorageManager = get()) }
}

val service = module {
    single<AuthenticationService> { get() }
}

val internalStorage = module {
    single { InternalStorageManager(androidContext()) }
}