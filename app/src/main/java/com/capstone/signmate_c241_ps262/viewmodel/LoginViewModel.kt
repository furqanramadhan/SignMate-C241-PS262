package com.capstone.signmate_c241_ps262.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.signmate_c241_ps262.response.Profile
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    private val _userProfile = MutableLiveData<Profile?>()
    val userProfile: LiveData<Profile?>
        get() = _userProfile

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: MutableLiveData<String?>
        get() = _toastMessage

    private fun fetchUserProfile(email: String? = auth.currentUser?.email) {
        email?.let {
            usersCollection.whereEqualTo("email", it)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val profile = document.toObject(Profile::class.java)
                        _userProfile.value = profile
                        break
                    }
                    _loginResult.value = true
                }
                .addOnFailureListener { exception ->
                    val errorMessage = "Error getting user profile: ${exception.message}"
                    _toastMessage.value = errorMessage
                    _loginResult.value = false
                }
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)!!
            signInWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            _toastMessage.value = "Google sign-in failed: ${e.message}"
            _loginResult.value = false
        }
    }
    private fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val profile = Profile(
                            id = it.uid,
                            email = it.email ?: "",
                            gender = "", // Retrieve gender if available
                            photo = it.photoUrl?.toString() ?: "",
                            phoneNumber = it.phoneNumber ?: "",
                            birthDate = "", // Retrieve birthDate if available
                            name = it.displayName ?: ""
                        )
                        saveUserProfile(profile)
                        if (profile.id != "Guest") {
                            fetchUserProfile() // Fetch profile only if user is not a guest
                        } else {
                            _loginResult.value = true // If guest, login is successful
                        }
                    }
                } else {
                    val errorMessage = "Authentication failed: ${task.exception?.message}"
                    _toastMessage.value = errorMessage
                    _loginResult.value = false
                }
            }
    }

    fun createGuestProfile(): Profile {
        return Profile(
            id = "Guest",
            email = "guest@example.com",
            gender = "Guest User",
            photo = "",
            phoneNumber = "",
            birthDate = "",
            name = "Guest User"
        )
    }

    private fun saveUserProfile(profile: Profile) {
        usersCollection.document(profile.id)
            .set(profile)
            .addOnSuccessListener {
                _toastMessage.value = "Welcome back ${profile.name}"
                _userProfile.value = profile
            }
            .addOnFailureListener { exception ->
                _toastMessage.value = "Error saving profile: ${exception.message}"
            }
    }
}