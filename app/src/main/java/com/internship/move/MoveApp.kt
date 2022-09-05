package com.internship.move

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoveApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoveApp)
            modules(
                listOf(
                    internalStorage,
                    repository,
                    viewModel,
                    service
                )
            )
        }
    }
}