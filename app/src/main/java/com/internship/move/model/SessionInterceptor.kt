package com.internship.move.model

import com.internship.move.model.providers.AuthenticationTokenProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class SessionInterceptor(
    private val authenticationTokenProvider: AuthenticationTokenProvider
//    private val internalStorageManager: UserDataInternalStorageManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val url = chain.request().url.toString()
        val token = authenticationTokenProvider.getAuthToken()

        if (token != null && !url.contains("login") && !url.contains("register")) {
            builder.header(AUTHORIZATION_KEY, AUTHORIZATION_BEARER_PREFIX + token)
        }
        return chain.proceed(builder.build())
    }

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
        private const val AUTHORIZATION_BEARER_PREFIX = "Bearer "
    }
}