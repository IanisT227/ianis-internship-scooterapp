package com.internship.move

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.internship.move.model.Repository

class MainViewModel(private val repo: Repository) : ViewModel() {

    suspend fun logIn() {
        repo.logIn()
    }

    suspend fun logOut() {
        repo.logOut()
    }

    suspend fun getLoggedStatus() = repo.getLoggedStatus()
}