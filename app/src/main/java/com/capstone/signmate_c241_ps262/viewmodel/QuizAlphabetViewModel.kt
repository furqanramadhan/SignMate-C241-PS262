package com.capstone.signmate_c241_ps262.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.signmate_c241_ps262.data.retrofit.ApiConfig
import com.capstone.signmate_c241_ps262.response.ListQuizAnswer
import com.capstone.signmate_c241_ps262.response.QuizQuestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizAlphabetViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiConfig.getApiService()

    private val _quizQuestions = MutableLiveData<List<QuizQuestion>>()
    val quizQuestions: LiveData<List<QuizQuestion>>
        get() = _quizQuestions

    private val _choiceAText = MutableLiveData<String>()
    val choiceAText: LiveData<String>
        get() = _choiceAText

    private val _choiceBText = MutableLiveData<String>()
    val choiceBText: LiveData<String>
        get() = _choiceBText

    private val _placeholderImageUrl = MutableLiveData<String>()
    val placeholderImageUrl: LiveData<String>
        get() = _placeholderImageUrl

    private val _submitQuizStatus = MutableLiveData<Boolean>()
    val submitQuizStatus: LiveData<Boolean>
        get() = _submitQuizStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private val totalQuestions = 10
    private var totalCorrectAnswers = 0
    private val selectedAnswers = mutableListOf<ListQuizAnswer>()


    init {
        loadQuizQuestions()
    }

    fun loadQuizQuestions() {
        apiService.getAlphabetQuizQuestions().enqueue(object : Callback<List<QuizQuestion>> {
            override fun onResponse(call: Call<List<QuizQuestion>>, response: Response<List<QuizQuestion>>) {
                if (response.isSuccessful) {
                    _quizQuestions.value = response.body()?.take(totalQuestions)
                    currentQuestionIndex = 0
                    _quizQuestions.value?.get(currentQuestionIndex)?.let { loadQuestionData(it) }
                } else {
                    _errorMessage.value = "Failed to fetch data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<QuizQuestion>>, t: Throwable) {
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }
    fun onAnswerSelected(selectedAnswer: String) {
        val currentQuestion = quizQuestions.value?.get(currentQuestionIndex)
        if (currentQuestion != null) {
            checkAnswer(selectedAnswer, currentQuestion)
        }

        val answer = ListQuizAnswer(currentQuestion?.id ?: "", selectedAnswer)
        selectedAnswers.add(answer)

        currentQuestionIndex++

        if (currentQuestionIndex < totalQuestions) {
            _quizQuestions.value?.get(currentQuestionIndex)?.let { nextQuestion ->
                loadQuestionData(nextQuestion)
            }
        } else {
            showQuizResults()
        }
    }


    private fun loadQuestionData(question: QuizQuestion) {
        _choiceAText.value = question.choices[0]
        _choiceBText.value = question.choices[1]
        _placeholderImageUrl.value = question.image
    }

    private fun checkAnswer(selectedAnswer: String, question: QuizQuestion) {
        if (selectedAnswer == question.answer) {
            correctAnswersCount++
        }
    }

    private fun showQuizResults() {
        val scoreValue = correctAnswersCount * 10 // Menghitung skor berdasarkan jumlah jawaban benar
        _score.value = scoreValue

        val totalQuestionsAnswered = selectedAnswers.size
        val totalCorrectAnswers = correctAnswersCount
        val totalIncorrectAnswers = totalQuestionsAnswered - totalCorrectAnswers

        val message = "Quiz completed!\n" +
                "Total questions: $totalQuestionsAnswered\n" +
                "Correct answers: $totalCorrectAnswers\n" +
                "Incorrect answers: $totalIncorrectAnswers\n" +
                "Score: $scoreValue"

        _errorMessage.value = message
        _submitQuizStatus.value = true
    }

    fun getScore(): Int {
        return totalCorrectAnswers
    }
}