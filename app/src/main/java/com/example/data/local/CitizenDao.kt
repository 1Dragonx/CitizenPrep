package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.data.model.MockTestAttempt
import com.example.data.model.Question
import com.example.data.model.QuestionProgress
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface CitizenDao {
    // --- Questions ---
    @Query("SELECT * FROM questions WHERE country = :country")
    fun getQuestionsByCountry(country: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE country = :country AND topicId = :topicId")
    fun getQuestionsByTopic(country: String, topicId: String): Flow<List<Question>>

    @Query("SELECT COUNT(*) FROM questions WHERE country = :country")
    suspend fun getQuestionCount(country: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    // --- Progress ---
    @Query("SELECT * FROM question_progress WHERE country = :country")
    fun getProgressByCountry(country: String): Flow<List<QuestionProgress>>

    @Query("SELECT * FROM question_progress WHERE questionId = :questionId LIMIT 1")
    suspend fun getProgressForQuestion(questionId: String): QuestionProgress?

    @Upsert
    suspend fun upsertProgress(progress: QuestionProgress)

    @Query("UPDATE question_progress SET isBookmarked = :bookmarked WHERE questionId = :questionId")
    suspend fun updateBookmark(questionId: String, bookmarked: Boolean)

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN question_progress p ON q.id = p.questionId
        WHERE q.country = :country AND p.isBookmarked = 1
    """)
    fun getBookmarkedQuestions(country: String): Flow<List<Question>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN question_progress p ON q.id = p.questionId
        WHERE q.country = :country AND p.isLastAnswerCorrect = 0 AND p.timesAnswered > 0
    """)
    fun getMistakeQuestions(country: String): Flow<List<Question>>

    // --- Mock Attempts ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMockAttempt(attempt: MockTestAttempt): Long

    @Query("SELECT * FROM mock_test_attempts WHERE country = :country ORDER BY timestamp DESC")
    fun getMockAttemptsByCountry(country: String): Flow<List<MockTestAttempt>>

    // --- Profile & Streak ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Upsert
    suspend fun upsertUserProfile(profile: UserProfile)

    @Query("DELETE FROM question_progress WHERE country = :country")
    suspend fun resetProgressForCountry(country: String)

    @Query("DELETE FROM mock_test_attempts WHERE country = :country")
    suspend fun resetMockAttemptsForCountry(country: String)
}
