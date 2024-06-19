package com.capstone.signmate_c241_ps262.ui.manageprofile

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.databinding.ActivityManageProfileBinding
import com.capstone.signmate_c241_ps262.response.Profile
import com.capstone.signmate_c241_ps262.viewmodel.ManageViewModel
import com.squareup.picasso.Picasso

class ManageProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageProfileBinding
    private lateinit var viewModel: ManageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#256656")


        viewModel = ViewModelProvider(this)[ManageViewModel::class.java]

        setupGenderSpinner()

        // Example usage:
        val userEmail = "contoh@email.com"
        viewModel.getUserProfile(userEmail)

        observeViewModel()
    }

    private fun setupGenderSpinner() {
        val genderArray = resources.getStringArray(R.array.gender_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle gender selection
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(this) { profile ->
            updateUI(profile)
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                showToast(it)
                viewModel.clearToastMessage()
            }
        }
    }

    private fun updateUI(profile: Profile?) {
        profile?.let {
            binding.etFullName.setText(it.name)
            binding.etPhoneNumber.setText(it.phoneNumber)

            // Set birth date components if applicable
            if (it.birthDate.isNotEmpty()) {
                val birthDateParts = it.birthDate.split("/")
                if (birthDateParts.size == 3) {
                    binding.etBirthDate.setText(birthDateParts[0])
                    binding.etBirthMonth.setText(birthDateParts[1])
                    binding.etBirthYear.setText(birthDateParts[2])
                }
            }

            // Set selected gender if applicable
            if (it.gender.isNotEmpty()) {
                val genderArray = resources.getStringArray(R.array.gender_array)
                val genderIndex = genderArray.indexOf(it.gender)
                if (genderIndex != -1) {
                    binding.spinnerGender.setSelection(genderIndex)
                }
            }
            // Load profile picture using Picasso
            if (it.photo.isNotEmpty()) {
                Picasso.get()
                    .load(it.photo)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_star)
                    .into(binding.ivProfile)
            } else {
                // If photo URL is empty, you can set a default image
                binding.ivProfile.setImageResource(R.drawable.ic_profile)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}