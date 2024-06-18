@file:Suppress("DEPRECATION")

package com.capstone.signmate_c241_ps262.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.databinding.ActivityLoginBinding
import com.capstone.signmate_c241_ps262.factory.ViewModelFactory
import com.capstone.signmate_c241_ps262.response.Profile
import com.capstone.signmate_c241_ps262.ui.dashboard.DashboardActivity
import com.capstone.signmate_c241_ps262.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)


        setupGoogleSignIn()

        // Initialize ViewModel with Factory
        val factory = ViewModelFactory(googleSignInClient)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnGuest.setOnClickListener{
            moveToDashboardWithGuestAccount()
        }

        // Observe ViewModel LiveData
        viewModel.userProfile.observe(this) { profile ->
            if (profile != null) {
                moveToDashboard(profile)
            }
        }

        viewModel.loginResult.observe(this) { success ->
            if (!success) {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        viewModel.handleSignInResult(task)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun moveToDashboard(profile: Profile) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("profile", profile)
        startActivity(intent)
        finish()
    }

    private fun moveToDashboardWithGuestAccount() {
        val guestProfile = viewModel.createGuestProfile()
        moveToDashboard(guestProfile)
    }

}