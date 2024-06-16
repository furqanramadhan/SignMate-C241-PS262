package com.capstone.signmate_c241_ps262.ui.play

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivityPlayNowAlphabetBinding

class PlayNowAlphabet : AppCompatActivity() {
    private lateinit var binding: ActivityPlayNowAlphabetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayNowAlphabetBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)
    }
}