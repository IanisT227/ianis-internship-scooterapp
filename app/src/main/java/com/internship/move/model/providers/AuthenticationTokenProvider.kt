package com.internship.move.model.providers

interface AuthenticationTokenProvider {
    fun getAuthToken(): String?
}