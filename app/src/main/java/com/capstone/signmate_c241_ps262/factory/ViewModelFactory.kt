package com.capstone.signmate_c241_ps262.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.signmate_c241_ps262.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class ViewModelFactory(
    private val googleSignInClient: GoogleSignInClient
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}