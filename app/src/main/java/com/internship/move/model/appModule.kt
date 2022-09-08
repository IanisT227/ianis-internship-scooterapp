package com.internship.move.model

import com.internship.move.feature.authentication.AuthenticationService
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val viewModel = module {
    viewModel { OnboardingViewModel(repo = get()) }
    viewModel { AuthenticationViewModel(get(), get()) }
}

val onBoardingRepository = module {
    single<OnBoardingRepository> { OnBoardingRepository(onBoardingInternalStorageManager = get()) }
}

val service = module {
    single<OkHttpClient> { provideHttpClient() }
    single<Moshi> { provideMoshi() }
    single<Retrofit> { provideRetrofit(get(), get()) }
    single<AuthenticationService> { provideAuthService(get()) }
}

val internalStorage = module {
    single { OnBoardingInternalStorageManager(androidContext()) }
}

fun provideAuthService(retrofit: Retrofit): AuthenticationService =
    retrofit.create(AuthenticationService::class.java)

fun provideRetrofit(moshi: Moshi, client: OkHttpClient) = Retrofit.Builder()
    .baseUrl(SERVER_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

fun provideMoshi(): Moshi = Moshi.Builder().build()

fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build()

private const val SERVER_URL = "https://move-scooter.herokuapp.com/api/"