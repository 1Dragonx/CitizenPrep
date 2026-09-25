package com.example

import com.example.data.ai.AiTutorService
import com.example.data.model.Country
import com.example.data.model.QuestionBank
import com.example.data.model.StudyLesson
import com.example.data.model.Topic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CitizenPrepUnitTest {

    @Test
    fun testCountryOfficialSpecifications() {
        // Canada official specs per Discover Canada
        val canada = Country.CANADA
        assertEquals("Canada", canada.displayName)
        assertEquals(20, canada.totalMockQuestions)
        assertEquals(15, canada.passingScore) // 15/20 passing score (75%)
        assertEquals(45, canada.timeLimitMinutes) // 45 minutes time limit

        // USA official specs per USCIS 2025 Civics test
        val usa = Country.USA
        assertEquals("United States", usa.displayName)
        assertEquals(20, usa.totalMockQuestions)
        assertEquals(12, usa.passingScore) // 12/20 passing target (60%)
    }

    @Test
    fun testQuestionBankIntegrity() {
        val questions = QuestionBank.ALL_QUESTIONS
        assertTrue("Question bank should have at least 40 questions", questions.size >= 40)

        val canadaQuestions = questions.filter { it.country == "CANADA" }
        val usaQuestions = questions.filter { it.country == "USA" }

        assertTrue("Canada questions count >= 20", canadaQuestions.size >= 20)
        assertTrue("USA questions count >= 20", usaQuestions.size >= 20)

        // Verify each question has 4 valid options, valid correct index, non-empty explanation & official source
        for (q in questions) {
            val options = q.getOptions()
            assertEquals("Each question must have 4 options", 4, options.size)
            assertTrue("Correct answer index must be 0..3", q.correctAnswerIndex in 0..3)
            assertTrue("Question text must not be empty", q.questionText.isNotBlank())
            assertTrue("Explanation EN must not be empty", q.explanationEn.isNotBlank())
            assertTrue("Explanation HI must not be empty", q.explanationHi.isNotBlank())
            assertTrue("Official source must not be empty", q.officialSource.isNotBlank())
        }
    }

    @Test
    fun testTopicSyllabusDefinitions() {
        val caTopics = Topic.getTopicsFor(Country.CANADA)
        val usTopics = Topic.getTopicsFor(Country.USA)

        assertEquals("Canada syllabus has 9 categories", 9, caTopics.size)
        assertEquals("USA syllabus has 9 categories", 9, usTopics.size)

        // Verify key Canada categories
        assertTrue(caTopics.any { it.title.contains("Rights & Responsibilities") })
        assertTrue(caTopics.any { it.title.contains("History") })
        assertTrue(caTopics.any { it.title.contains("Government") })

        // Verify key USA categories
        assertTrue(usTopics.any { it.title.contains("American Government") })
        assertTrue(usTopics.any { it.title.contains("Constitution") })
        assertTrue(usTopics.any { it.title.contains("Branches") })
    }

    @Test
    fun testStudyLessonsAvailable() {
        val caLessons = StudyLesson.getLessonsFor(Country.CANADA)
        val usLessons = StudyLesson.getLessonsFor(Country.USA)

        assertTrue(caLessons.isNotEmpty())
        assertTrue(usLessons.isNotEmpty())

        for (lesson in caLessons + usLessons) {
            assertTrue(lesson.keyFacts.isNotEmpty())
            assertTrue(lesson.memoryTip.isNotBlank())
        }
    }

    @Test
    fun testAiTutorKnowledgeResponse() = runBlocking {
        val tutor = AiTutorService()

        // Test English query
        val responseEn = tutor.getTutorResponse("Tell me about parliament", Country.CANADA, "en")
        assertNotNull(responseEn)
        assertTrue(responseEn.text.contains("Parliament") || responseEn.text.contains("Sovereign"))

        // Test Hindi query
        val responseHi = tutor.getTutorResponse("संसद के बारे में बताओ", Country.CANADA, "hi")
        assertNotNull(responseHi)
        assertTrue(responseHi.text.contains("संसद") || responseHi.text.contains("कनाडा"))

        // Test US Branches query in Hindi
        val responseUsHi = tutor.getTutorResponse("Branches of government", Country.USA, "hi")
        assertNotNull(responseUsHi)
        assertTrue(responseUsHi.text.contains("शाखाएं") || responseUsHi.text.contains("Congress") || responseUsHi.text.contains("अमेरिकी"))
    }
}
