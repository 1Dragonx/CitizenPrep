package com.example.data.repository

import com.example.data.local.CitizenDao
import com.example.data.model.Country
import com.example.data.model.MockTestAttempt
import com.example.data.model.Question
import com.example.data.model.QuestionBank
import com.example.data.model.QuestionProgress
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class CitizenRepository(private val dao: CitizenDao) {

    suspend fun checkAndSeedDatabase() {
        val caCount = dao.getQuestionCount("CANADA")
        val usCount = dao.getQuestionCount("USA")
        if (caCount == 0 || usCount == 0) {
            dao.insertQuestions(QuestionBank.ALL_QUESTIONS)
        }
    }

    fun getQuestionsByCountry(country: Country): Flow<List<Question>> {
        return dao.getQuestionsByCountry(country.name)
    }

    fun getQuestionsByTopic(country: Country, topicId: String): Flow<List<Question>> {
        return dao.getQuestionsByTopic(country.name, topicId)
    }

    fun getProgressByCountry(country: Country): Flow<List<QuestionProgress>> {
        return dao.getProgressByCountry(country.name)
    }

    fun getBookmarkedQuestions(country: Country): Flow<List<Question>> {
        return dao.getBookmarkedQuestions(country.name)
    }

    fun getMistakeQuestions(country: Country): Flow<List<Question>> {
        return dao.getMistakeQuestions(country.name)
    }

    fun getMockAttempts(country: Country): Flow<List<MockTestAttempt>> {
        return dao.getMockAttemptsByCountry(country.name)
    }

    fun getUserProfile(): Flow<UserProfile?> {
        return dao.getUserProfile()
    }

    suspend fun saveAnswer(
        question: Question,
        selectedOptionIndex: Int,
        isCorrect: Boolean
    ) {
        val existing = dao.getProgressForQuestion(question.id)
        val currentTimesAnswered = (existing?.timesAnswered ?: 0) + 1
        val currentTimesCorrect = (existing?.timesCorrect ?: 0) + (if (isCorrect) 1 else 0)

        val progress = QuestionProgress(
            questionId = question.id,
            country = question.country,
            topicId = question.topicId,
            timesAnswered = currentTimesAnswered,
            timesCorrect = currentTimesCorrect,
            lastSelectedOption = selectedOptionIndex,
            isLastAnswerCorrect = isCorrect,
            isBookmarked = existing?.isBookmarked ?: false,
            lastAnsweredTimestamp = System.currentTimeMillis()
        )
        dao.upsertProgress(progress)
    }

    suspend fun toggleBookmark(questionId: String, currentBookmarked: Boolean, country: String, topicId: String) {
        val existing = dao.getProgressForQuestion(questionId)
        if (existing == null) {
            dao.upsertProgress(
                QuestionProgress(
                    questionId = questionId,
                    country = country,
                    topicId = topicId,
                    isBookmarked = !currentBookmarked
                )
            )
        } else {
            dao.updateBookmark(questionId, !currentBookmarked)
        }
    }

    suspend fun recordMockTestAttempt(attempt: MockTestAttempt): Long {
        return dao.insertMockAttempt(attempt)
    }

    suspend fun updateProfile(profile: UserProfile) {
        dao.upsertUserProfile(profile)
    }

    suspend fun checkAndUpdateStreak(currentProfile: UserProfile): UserProfile {
        val now = System.currentTimeMillis()
        val lastActive = currentProfile.lastActiveDateMillis
        val calNow = Calendar.getInstance().apply { timeInMillis = now }
        val calLast = Calendar.getInstance().apply { timeInMillis = lastActive }

        val diffDays = (calNow.get(Calendar.DAY_OF_YEAR) - calLast.get(Calendar.DAY_OF_YEAR)) +
                365 * (calNow.get(Calendar.YEAR) - calLast.get(Calendar.YEAR))

        val newStreak = when (diffDays) {
            0 -> currentProfile.streakDays // same day
            1 -> currentProfile.streakDays + 1 // continuous day!
            else -> 1 // broken streak or fresh start
        }

        val updated = currentProfile.copy(
            streakDays = newStreak,
            lastActiveDateMillis = now
        )
        dao.upsertUserProfile(updated)
        return updated
    }

    suspend fun resetCountryData(country: Country) {
        dao.resetProgressForCountry(country.name)
        dao.resetMockAttemptsForCountry(country.name)
    }
}
