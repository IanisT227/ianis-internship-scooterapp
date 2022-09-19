package com.internship.move.feature.licenseRegistration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.model.UserDataInternalStorageManager
import com.internship.move.utils.logTag
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class LicenseRegistrationViewModel(
    private val userDataInternalStorageManager: UserDataInternalStorageManager,
    private val licenseService: LicenseService
) : ViewModel() {
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isError: LiveData<Boolean>
        get() = _isError

    fun uploadImage(token: String, image: File) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val licenseResp = licenseService.uploadLicense(
                    token = "Bearer $token",
                    driverLicenseKey = MultipartBody.Part.createFormData(
                        "driverLicenseKey",
                        image.name,
                        image.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                )
                userDataInternalStorageManager.uploadLicensePicture(image.path)
                logTag("IMAGE_RESPONSE", licenseResp.toString())
            } catch (e: Exception) {
                logTag("IMAGE_RESPONSE", e.toString())
                _isError.value = true
            } finally {
                isLoading.value = false
                _isError.value = false
            }
        }
    }
}