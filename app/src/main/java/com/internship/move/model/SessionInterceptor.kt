package com.internship.move.model

import com.internship.move.model.providers.AuthenticationTokenProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class SessionInterceptor(
    private val authenticationTokenProvider: AuthenticationTokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val url = chain.request().url.toString()
        val token = authenticationTokenProvider.getAuthToken()

        if (token != null && !url.contains(REQUEST_EXCEPTION_LOGIN) && !url.contains(REQUEST_EXCEPTION_REGISTER)) {
            builder.header(AUTHORIZATION_KEY, AUTHORIZATION_BEARER_PREFIX + token)
        }
        return chain.proceed(builder.build())
    }

    companion object {
        private const val REQUEST_EXCEPTION_LOGIN = "login"
        private const val REQUEST_EXCEPTION_REGISTER = "register"
        private const val AUTHORIZATION_KEY = "Authorization"
        private const val AUTHORIZATION_BEARER_PREFIX = "Bearer "
    }
}