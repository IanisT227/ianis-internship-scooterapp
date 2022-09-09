package com.internship.move.feature.licenseRegistration

import android.media.Image
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.model.UserDataInternalStorageManager
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.RequestBody





class LicenseRegistrationViewModel(
    private val userDataInternalStorageManager: UserDataInternalStorageManager,
    private val licenseService: LicenseService
) : ViewModel() {

    fun uploadImage(token: String, image: Image) {
        viewModelScope.launch {
           // licenseService.uploadLicense(token = "Bearer $token", image = MultipartBody.Part.createFormData("driverLicenseKey", image.getName(), requestFile() ))
        }
    }

}