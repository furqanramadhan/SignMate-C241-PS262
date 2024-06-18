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

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Set up click listeners
        setupMenuButtonListeners()

        // Observe ViewModel LiveData
        observeViewModel()
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
        // Update profile image (assuming `photo` field in Profile is a URL)
        profile.photo?.let {
            Picasso.get().load(it).placeholder(R.drawable.ic_profile).into(binding.ivProfile)
        }

        // Set welcome text
        binding.tvWelcome.text = getString(R.string.welcome, profile.name)
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