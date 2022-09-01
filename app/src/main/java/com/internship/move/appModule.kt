package com.internship.move

import com.internship.move.model.Repository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel { MainViewModel(repo = get()) }
}

val repository = module {
    single<Repository> { Repository(internalStorageManager = get()) }
}

val internalStorage = module {
    single { InternalStorageManager(androidContext()) }
}