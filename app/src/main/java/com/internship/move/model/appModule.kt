package com.internship.move.model

import com.internship.move.BuildConfig
import com.internship.move.feature.authentication.AuthenticationService
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.licenseRegistration.LicenseRegistrationViewModel
import com.internship.move.feature.licenseRegistration.LicenseService
import com.internship.move.feature.map.ScooterService
import com.internship.move.feature.map.ScooterViewModel
import com.internship.move.feature.onboarding.OnboardingViewModel
import com.internship.move.model.providers.AuthenticationTokenProvider
import com.internship.move.model.providers.RuntimeAuthenticationTokenProvider
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
    viewModel { ScooterViewModel(get()) }
}

val onBoardingRepository = module {
    single<OnBoardingRepository> { OnBoardingRepository(userDataInternalStorageManager = get()) }
}

val service = module {
    single<OkHttpClient> { provideHttpClient(get()) }
    single<Moshi> { provideMoshi() }
    single<Retrofit> { provideRetrofit(get(), get()) }
    single<AuthenticationService> { provideAuthService(get()) }
    single<LicenseService> { provideLicenseService(get()) }
    single<ScooterService> { provideMapService(get()) }
}

val tokenProvider = module { single<AuthenticationTokenProvider> { RuntimeAuthenticationTokenProvider(get()) } }

val internalStorage = module {
    single { UserDataInternalStorageManager(androidContext(), get()) }
}

private fun provideAuthService(retrofit: Retrofit): AuthenticationService =
    retrofit.create(AuthenticationService::class.java)

private fun provideLicenseService(retrofit: Retrofit): LicenseService = retrofit.create(LicenseService::class.java)

private fun provideMapService(retrofit: Retrofit): ScooterService = retrofit.create(ScooterService::class.java)

private fun provideRetrofit(moshi: Moshi, client: OkHttpClient) = Retrofit.Builder()
    .baseUrl(SERVER_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

private fun provideMoshi(): Moshi = Moshi.Builder().build()

private fun provideHttpClient(tokenProvider: AuthenticationTokenProvider): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        httpClient.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.addNetworkInterceptor(SessionInterceptor(tokenProvider))
    }
    return httpClient.build()
}

private const val SERVER_URL = "https://move-scooter.herokuapp.com/api/"