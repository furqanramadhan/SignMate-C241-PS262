package com.capstone.signmate_c241_ps262.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.signmate_c241_ps262.data.retrofit.ApiConfig
import com.capstone.signmate_c241_ps262.response.Profile
import com.capstone.signmate_c241_ps262.response.ProfileEditResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = ApiConfig.getApiService()

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile>
        get() = _profile

    private val _editProfileStatus = MutableLiveData<Boolean>()
    val editProfileStatus: LiveData<Boolean>
        get() = _editProfileStatus

    fun getUserProfile(email: String) {
        apiService.getUserProfile(email).enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful) {
                    _profile.postValue(response.body())
                } else {
                    showToast("Failed to get profile: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    fun updateUserProfile(profile: Profile) {
        apiService.editUserProfile(profile).enqueue(object : Callback<ProfileEditResponse> {
            override fun onResponse(
                call: Call<ProfileEditResponse>,
                response: Response<ProfileEditResponse>
            ) {
                if (response.isSuccessful) {
                    _editProfileStatus.postValue(true)
                    showToast("Profile updated successfully")
                } else {
                    _editProfileStatus.postValue(false)
                    showToast("Failed to update profile: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfileEditResponse>, t: Throwable) {
                _editProfileStatus.postValue(false)
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}