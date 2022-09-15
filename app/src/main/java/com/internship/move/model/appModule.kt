package com.internship.move.model

import com.internship.move.feature.authentication.AuthenticationService
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.licenseRegistration.LicenseRegistrationViewModel
import com.internship.move.feature.licenseRegistration.LicenseService
import com.internship.move.feature.map.MapService
import com.internship.move.feature.map.MapViewModel
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
    viewModel { LicenseRegistrationViewModel(get(), get()) }
    viewModel { MapViewModel(get()) }
}

val onBoardingRepository = module {
    single<OnBoardingRepository> { OnBoardingRepository(userDataInternalStorageManager = get()) }
}

val service = module {
    single<OkHttpClient> { provideHttpClient() }
    single<Moshi> { provideMoshi() }
    single<Retrofit> { provideRetrofit(get(), get()) }
    single<AuthenticationService> { provideAuthService(get()) }
    single<LicenseService> { provideLicenseService(get()) }
    single<MapService> { provideMapService(get()) }
}

val internalStorage = module {
    single { UserDataInternalStorageManager(androidContext(), get()) }
}

fun provideAuthService(retrofit: Retrofit): AuthenticationService =
    retrofit.create(AuthenticationService::class.java)

fun provideLicenseService(retrofit: Retrofit): LicenseService = retrofit.create(LicenseService::class.java)

fun provideMapService(retrofit: Retrofit): MapService = retrofit.create(MapService::class.java)

fun provideRetrofit(moshi: Moshi, client: OkHttpClient) = Retrofit.Builder()
    .baseUrl(SERVER_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

fun provideMoshi(): Moshi = Moshi.Builder().build()

fun provideHttpClient(): OkHttpClient =
    OkHttpClient.Builder().addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

private const val SERVER_URL = "https://move-scooter.herokuapp.com/api/"