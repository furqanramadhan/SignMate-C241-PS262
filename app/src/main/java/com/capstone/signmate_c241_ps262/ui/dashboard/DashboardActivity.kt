package com.capstone.signmate_c241_ps262.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.databinding.ActivityDashboardBinding
import com.capstone.signmate_c241_ps262.response.Profile
import com.capstone.signmate_c241_ps262.ui.about.AboutActivity
import com.capstone.signmate_c241_ps262.ui.manageprofile.ManageProfileActivity
import com.capstone.signmate_c241_ps262.viewmodel.DashboardViewModel
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var guestProfile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Retrieve guest profile from intent extras
        guestProfile = intent.getParcelableExtra("profile") ?: Profile()

        // Set up click listeners
        setupMenuButtonListeners()

        // Observe ViewModel LiveData
        observeViewModel()

        updateUI(guestProfile)
    }

    private fun setupMenuButtonListeners() {
        binding.btnMenu1.setOnClickListener {
            navigateToManageProfile()
        }

        binding.btnMenu2.setOnClickListener {
            navigateToAbout()
        }

        binding.btnMenu3.setOnClickListener {
            navigateToFeedback()
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(this) { profile ->
            profile?.let { updateUI(it) }
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let { showToast(it) }
        }

        // Fetch user profile
        viewModel.fetchUserProfile()
    }

    @SuppressLint("StringFormatInvalid")
    private fun updateUI(profile: Profile) {
        // Check if user is guest
        if (profile.id == "Guest") {
            // If user is guest, set default profile image
            Picasso.get().load(R.drawable.ic_profile).into(binding.ivProfile)

            // Set welcome text for guest
            binding.tvWelcome.text = getString(R.string.welcome_guest)
        } else {
            // If user is not guest, check if photo URL is not empty before loading it
            if (!profile.photo.isNullOrEmpty()) {
                Picasso.get().load(profile.photo).placeholder(R.drawable.ic_profile).into(binding.ivProfile)
            } else {
                // Set default profile image if photo URL is empty
                Picasso.get().load(R.drawable.ic_profile).into(binding.ivProfile)
            }

            // Set welcome text for logged-in user
            binding.tvWelcome.text = getString(R.string.welcome, profile.name)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToManageProfile() {
        val intent = Intent(this, ManageProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAbout() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToFeedback() {
        val feedbackUrl = "https://example.com/feedback" // Replace with your feedback URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(feedbackUrl))
        startActivity(intent)
    }
}