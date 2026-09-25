package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey
    val id: String,
    val country: String, // "CANADA" or "USA"
    val topicId: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswerIndex: Int, // 0..3
    val explanationEn: String,
    val explanationHi: String,
    val explanationFr: String,
    val explanationEs: String,
    val officialSource: String,
    val difficulty: String = "medium" // easy, medium, hard
) {
    fun getOptions(): List<String> = listOf(optionA, optionB, optionC, optionD)

    fun getExplanation(languageCode: String): String {
        return when (languageCode.lowercase()) {
            "hi", "hindi" -> explanationHi
            "fr", "french" -> explanationFr
            "es", "spanish" -> explanationEs
            else -> explanationEn
        }
    }
}

@Entity(tableName = "question_progress")
data class QuestionProgress(
    @PrimaryKey
    val questionId: String,
    val country: String,
    val topicId: String,
    val timesAnswered: Int = 0,
    val timesCorrect: Int = 0,
    val lastSelectedOption: Int = -1,
    val isLastAnswerCorrect: Boolean = false,
    val isBookmarked: Boolean = false,
    val lastAnsweredTimestamp: Long = 0L
)

@Entity(tableName = "mock_test_attempts")
data class MockTestAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val country: String,
    val score: Int,
    val totalQuestions: Int,
    val passingScore: Int,
    val passed: Boolean,
    val timeSpentSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val selectedCountry: String = "CANADA",
    val userName: String = "Applicant",
    val preferredLanguage: String = "en", // en, hi, fr, es
    val dailyGoalQuestions: Int = 15,
    val examDateMillis: Long = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000), // 30 days from now
    val streakDays: Int = 1,
    val lastActiveDateMillis: Long = System.currentTimeMillis(),
    val onboardingCompleted: Boolean = false
)
