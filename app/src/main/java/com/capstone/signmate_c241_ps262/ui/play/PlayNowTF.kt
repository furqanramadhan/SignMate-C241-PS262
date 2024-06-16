package com.capstone.signmate_c241_ps262.ui.play

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.databinding.ActivityPlayNowNumberBinding
import com.capstone.signmate_c241_ps262.databinding.ActivityPlayNowTfBinding

class PlayNowTF : AppCompatActivity() {
    private lateinit var binding: ActivityPlayNowTfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayNowTfBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)
    }
}