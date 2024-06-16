package com.capstone.signmate_c241_ps262.ui.quiz

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivityQuizTfactivityBinding

class QuizTFActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizTfactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizTfactivityBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)
    }
}