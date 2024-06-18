package com.capstone.signmate_c241_ps262.response

data class QuizQuestion(
    val id: String,
    val question: String,
    val answer: String,
    val choice: List<String>,
    val image: String
)
