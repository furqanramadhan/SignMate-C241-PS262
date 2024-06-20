package com.capstone.signmate_c241_ps262.ui.play

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.databinding.ActivityPlayNowNumberBinding
import com.capstone.signmate_c241_ps262.response.QuizQuestion
import com.capstone.signmate_c241_ps262.viewmodel.QuizNumberViewModel

class PlayNowNumber : AppCompatActivity() {
    private lateinit var binding: ActivityPlayNowNumberBinding
    private lateinit var viewModel: QuizNumberViewModel
    private lateinit var currentQuestion: QuizQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayNowNumberBinding.inflate(layoutInflater)
        window.statusBarColor = Color.parseColor("#256656")
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[QuizNumberViewModel::class.java]

        viewModel.quizQuestions.observe(this) { quizQuestions ->
            if (quizQuestions.isNotEmpty()) {
                currentQuestion = quizQuestions[0]
                loadQuestion(currentQuestion)
            }
        }

        viewModel.submitQuizStatus.observe(this) { submitted ->
            if (submitted) {
                viewModel.errorMessage.value?.let { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
                viewModel.score.value?.let { score ->
                    Toast.makeText(this, "Your score is: $score", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnChoiceA.setOnClickListener {
            onAnswerSelected(binding.btnChoiceA.text.toString())
        }

        binding.btnChoiceB.setOnClickListener {
            onAnswerSelected(binding.btnChoiceB.text.toString())
        }

        viewModel.loadQuizQuestions()
    }
    private fun loadQuestion(question: QuizQuestion) {
        Glide.with(this)
            .load(question.image)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_brokenimg)
            .into(binding.ivPlaceholderquiz)

        binding.tvWait.text = question.question
        if (question.choices.size >= 2) {
            binding.btnChoiceA.text = question.choices[0]
            binding.btnChoiceB.text = question.choices[1]
        }
    }
    private fun onAnswerSelected(selectedAnswer: String) {
        viewModel.onAnswerSelected(selectedAnswer)

        if ((viewModel.quizQuestions.value?.size ?: 0) > viewModel.currentQuestionIndex) {
            val nextQuestion = viewModel.quizQuestions.value?.get(viewModel.currentQuestionIndex)
            if (nextQuestion != null) {
                loadQuestion(nextQuestion)
            }
        }
    }
}
