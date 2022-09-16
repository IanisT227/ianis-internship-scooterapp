package com.internship.move

import android.app.Application
import com.internship.move.model.internalStorage
import com.internship.move.model.onBoardingRepository
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
                    onBoardingRepository,
                    viewModel,
                    service,
                    tokenProvider
                )
            )
        }
    }
}