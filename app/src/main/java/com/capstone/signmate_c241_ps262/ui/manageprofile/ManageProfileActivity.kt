package com.capstone.signmate_c241_ps262.ui.manageprofile

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivityManageProfileBinding
import com.capstone.signmate_c241_ps262.response.Profile
import com.capstone.signmate_c241_ps262.ui.dashboard.DashboardActivity
import com.capstone.signmate_c241_ps262.viewmodel.ManageViewModel
import com.google.firebase.auth.FirebaseAuth

class ManageProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageProfileBinding
    private val viewModel: ManageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize spinner with gender options
        val genderOptions = arrayOf("Male", "Female")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        // Observe changes in profile data
        viewModel.profile.observe(this) { profile ->
            profile?.let { updateUI(it) }
        }

        // Observe edit profile status
        viewModel.editProfileStatus.observe(this) { success ->
            if (success) {
                showToast("Profile updated successfully")
            } else {
                showToast("Failed to update profile")
            }
        }

        val userEmail = getCurrentUserEmail()
        if (userEmail != null) {
            viewModel.getUserProfile(userEmail)
        } else {
            showToast("Error: User email not found")
        }
        // Update user profile
        binding.btnSaveprofile.setOnClickListener {
            val updatedProfile = gatherUpdatedProfileFromUI()
            if (updatedProfile.id.isBlank()) {
                showToast("Error: Profile ID is missing")
            } else {
                viewModel.updateUserProfile(updatedProfile)
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(profile: Profile) {
        binding.tvFullname.text = profile.name
        binding.tvEmail.text = profile.email
        binding.etFullName.setText(profile.name)
        binding.etPhoneNumber.setText(profile.phoneNumber)
        binding.spinnerGender.setSelection(getSpinnerIndex(binding.spinnerGender, profile.gender))
        val birthDateParts = profile.birthDate.split("-")
        if (birthDateParts.size == 3) {
            binding.etBirthDate.setText(birthDateParts[0])
            binding.etBirthMonth.setText(birthDateParts[1])
            binding.etBirthYear.setText(birthDateParts[2])
        }
    }

    private fun gatherUpdatedProfileFromUI(): Profile {
        val currentProfile = viewModel.profile.value ?: Profile()
        return Profile(
            id = currentProfile.id,
            name = binding.etFullName.text.toString().trim(),
            email = currentProfile.email,
            gender = binding.spinnerGender.selectedItem.toString(),
            phoneNumber = binding.etPhoneNumber.text.toString().trim(),
            birthDate = "${binding.etBirthDate.text}-${binding.etBirthMonth.text}-${binding.etBirthYear.text}".trim(),
            photo = currentProfile.photo
        )
    }

    private fun getSpinnerIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(value, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun getCurrentUserEmail(): String? {
        // Implementasi untuk mendapatkan email pengguna yang sedang login
        // Misalnya menggunakan Firebase Authentication
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        return currentUser?.email
    }

}