package com.capstone.signmate_c241_ps262.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.signmate_c241_ps262.response.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    private val _userProfile = MutableLiveData<Profile?>()
    val userProfile: LiveData<Profile?>
        get() = _userProfile

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: MutableLiveData<String?>
        get() = _toastMessage

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.email?.let { email ->
            // Check if the current user is not a guest
            if (!isGuestUser(currentUser.uid)) {
                usersCollection.whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val profile = document.toObject(Profile::class.java)
                            _userProfile.value = profile
                            break
                        }
                    }
                    .addOnFailureListener { exception ->
                        val errorMessage = "Error getting user profile: ${exception.message}"
                        _toastMessage.value = errorMessage
                    }
            }
        }
    }
    private fun isGuestUser(userId: String): Boolean {
        return userId == "Guest"
    }
}