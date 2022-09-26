package com.internship.move

import android.app.Application
import com.internship.move.model.internalStorage
import com.internship.move.model.repository
import com.internship.move.model.service
import com.internship.move.model.tokenProvider
import com.internship.move.model.viewModel
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
                    service,
                    tokenProvider
                )
            )
        }
    }
}