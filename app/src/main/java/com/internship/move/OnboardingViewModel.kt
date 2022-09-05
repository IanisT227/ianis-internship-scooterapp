package com.internship.move

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.model.Repository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repo: Repository) : ViewModel() {

    suspend fun getLoggedStatus() = repo.getLoggedStatus()

    suspend fun changeLogStatus(logValue: Boolean) = repo.changeLoggedStatus(logValue)
}