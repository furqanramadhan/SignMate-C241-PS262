package com.capstone.signmate_c241_ps262.data.retrofit

import com.capstone.signmate_c241_ps262.response.ListQuizAnswer
import com.capstone.signmate_c241_ps262.response.Profile
import com.capstone.signmate_c241_ps262.response.ProfileEditResponse
import com.capstone.signmate_c241_ps262.response.QuizAnswer
import com.capstone.signmate_c241_ps262.response.QuizQuestion
import com.capstone.signmate_c241_ps262.response.QuizResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/alphabet-quiz/questions")
    fun getAlphabetQuizQuestions(
        @Query("limit") limit: Int = 10
    ): Call<List<QuizQuestion>>

    // Endpoint Alphabet Quiz - Post
    @POST("/alphabet-quiz/submit")
    fun submitAlphabetQuizAnswers(
        @Body answers: QuizAnswer
    ): Call<QuizResponse>

    // Endpoint Number Quiz - Get
    @GET("/number-quiz/questions")
    fun getNumberQuizQuestions(): Call<List<QuizQuestion>>

    // Endpoint Number Quiz - Post
    @POST("/number-quiz/submit")
    fun submitNumberQuizAnswers(
        @Body answers: QuizAnswer
    ): Call<QuizResponse>

    // Endpoint Yes or No Quiz - Get
    @GET("/yes-or-no-quiz/questions")
    fun getYesOrNoQuizQuestions(): Call<List<QuizQuestion>>

    // Endpoint Yes or No Quiz - Post
    @POST("/yes-or-no-quiz/submit")
    fun submitYesOrNoQuizAnswers(
        @Body answers: QuizAnswer
    ): Call<QuizResponse>

    // Endpoint Edit Profile - Get
    @GET("/user/profile")
    fun getUserProfile(
        @Query("email") email: String
    ): Call<Profile>

    // Endpoint Edit Profile - Post
    @POST("/user/edit")
    fun editUserProfile(
        @Body profile: Profile
    ): Call<ProfileEditResponse>
}
