package com.capstone.signmate_c241_ps262.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.signmate_c241_ps262.data.retrofit.ApiConfig
import com.capstone.signmate_c241_ps262.firestore.FirestoreService
import com.capstone.signmate_c241_ps262.response.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ManageViewModel(@SuppressLint("StaticFieldLeak") private val context: Context) : ViewModel() {
    private val firestoreService = FirestoreService(context)
    private val apiService = ApiConfig.getApiService()

    private val _userProfile = MutableLiveData<Profile?>()
    val userProfile: LiveData<Profile?>
        get() = _userProfile

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?>
        get() = _toastMessage

    fun getUserProfile(email: String) {
        viewModelScope.launch {
            val profile = withContext(Dispatchers.IO) {
                firestoreService.getUserProfileByEmail(email)
            }
            _userProfile.value = profile
        }
    }

    fun updateUserProfile(profile: Profile) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    apiService.editUserProfile(profile).awaitResponse()
                } catch (e: Exception) {
                    null
                }
            }
            if (response != null && response.isSuccessful) {
                _userProfile.value = profile
                _toastMessage.value = "Profile updated successfully"
            } else {
                // Handle unsuccessful response
                _toastMessage.value = "Failed to update profile: ${response?.errorBody()?.string()}"
            }
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}