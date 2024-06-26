package com.capstone.signmate_c241_ps262.ui.quiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivityQuizAlphabetBinding
import com.capstone.signmate_c241_ps262.ui.play.PlayNowAlphabet

class QuizAlphabetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizAlphabetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizAlphabetBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)

        setupMenuButtonListeners()
    }

    private fun setupMenuButtonListeners() {
        binding.btnPlayNow.setOnClickListener {
            navigateToPlayNowAlphabet()
        }
    }

    private fun navigateToPlayNowAlphabet(){
        val intent = Intent(this, PlayNowAlphabet::class.java)
        startActivity(intent)
    }

}