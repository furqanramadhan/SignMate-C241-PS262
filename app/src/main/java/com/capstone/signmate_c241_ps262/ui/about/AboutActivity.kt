package com.capstone.signmate_c241_ps262.ui.about

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)

    }
}
