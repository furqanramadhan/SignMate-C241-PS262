package com.capstone.signmate_c241_ps262.ui.quiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivityQuizNumberBinding
import com.capstone.signmate_c241_ps262.databinding.ActivityQuizTfactivityBinding
import com.capstone.signmate_c241_ps262.ui.play.PlayNowNumber
import com.capstone.signmate_c241_ps262.ui.play.PlayNowTF

class QuizTFActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizTfactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizTfactivityBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)

        setupMenuButtonListeners()
    }

    private fun setupMenuButtonListeners() {
        binding.btnPlayNow.setOnClickListener {
            navigateToPlayNowNumber()
        }
    }

    private fun navigateToPlayNowNumber(){
        val intent = Intent(this, PlayNowTF::class.java)
        startActivity(intent)
    }
}