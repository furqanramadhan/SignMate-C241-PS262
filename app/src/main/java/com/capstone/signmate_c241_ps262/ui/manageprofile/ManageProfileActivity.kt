package com.capstone.signmate_c241_ps262.ui.manageprofile

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivityManageProfileBinding

class ManageProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProfileBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)

    }
}