package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiTutorService
import com.example.data.ai.TutorMessage
import com.example.data.local.CitizenDatabase
import com.example.data.model.Country
import com.example.data.model.MockTestAttempt
import com.example.data.model.Question
import com.example.data.model.QuestionBank
import com.example.data.model.QuestionProgress
import com.example.data.model.StudyLesson
import com.example.data.model.Topic
import com.example.data.model.UserProfile
import com.example.data.repository.CitizenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    ONBOARDING,
    DASHBOARD,
    PRACTICE_HUB,
    STUDY_GUIDE,
    QUIZ,
    QUIZ_RESULT,
    AI_TUTOR,
    ANALYTICS,
    SETTINGS
}

enum class QuizMode {
    MOCK,
    TOPIC,
    QUICK_10,
    MISTAKES,
    FLASHCARDS
}

data class QuizSessionState(
    val mode: QuizMode = QuizMode.QUICK_10,
    val topicId: String? = null,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> selectedOptionIndex (0..3)
    val flaggedIndexes: Set<Int> = emptySet(),
    val timeRemainingSeconds: Int = 0,
    val totalTimeSeconds: Int = 0,
    val isTimerActive: Boolean = false,
    val isFinished: Boolean = false,
    val score: Int = 0,
    val passed: Boolean = false,
    val timeSpentSeconds: Int = 0
)

data class TopicMastery(
    val topic: Topic,
    val attemptedCount: Int,
    val correctCount: Int,
    val masteryPercentage: Int // 0..100
)

class CitizenPrepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CitizenRepository
    private val tutorService = AiTutorService()

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedCountry = MutableStateFlow(Country.CANADA)
    val selectedCountry: StateFlow<Country> = _selectedCountry.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _quizSession = MutableStateFlow(QuizSessionState())
    val quizSession: StateFlow<QuizSessionState> = _quizSession.asStateFlow()

    private val _tutorMessages = MutableStateFlow<List<TutorMessage>>(emptyList())
    val tutorMessages: StateFlow<List<TutorMessage>> = _tutorMessages.asStateFlow()

    private val _isTutorThinking = MutableStateFlow(false)
    val isTutorThinking: StateFlow<Boolean> = _isTutorThinking.asStateFlow()

    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private var timerJob: Job? = null

    init {
        val database = CitizenDatabase.getDatabase(application)
        repository = CitizenRepository(database.citizenDao())

        viewModelScope.launch {
            repository.checkAndSeedDatabase()
            // Observe profile
            repository.getUserProfile().collect { profile ->
                if (profile != null) {
                    _userProfile.value = profile
                    _selectedCountry.value = if (profile.selectedCountry == "USA") Country.USA else Country.CANADA
                    _currentLanguage.value = profile.preferredLanguage
                    if (!profile.onboardingCompleted) {
                        _currentScreen.value = AppScreen.ONBOARDING
                    }
                } else {
                    // Seed initial profile
                    val initial = UserProfile()
                    repository.updateProfile(initial)
                    _currentScreen.value = AppScreen.ONBOARDING
                }
            }
        }

        initTutorWelcome()
    }

    private fun initTutorWelcome() {
        val welcome = TutorMessage(
            sender = TutorMessage.Sender.TUTOR,
            text = "Welcome to CitizenPrep AI Tutor! Ask me anything about Canadian or U.S. citizenship tests, rights, parliament, history, or legal concepts in English, Hindi, French, or Spanish.",
            languageCode = _currentLanguage.value,
            officialReference = "Discover Canada & USCIS Civics Official Guides"
        )
        _tutorMessages.value = listOf(welcome)
    }

    // Questions and progress flows for current country
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentQuestions: StateFlow<List<Question>> = _selectedCountry
        .flatMapLatest { country -> repository.getQuestionsByCountry(country) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentProgress: StateFlow<List<QuestionProgress>> = _selectedCountry
        .flatMapLatest { country -> repository.getProgressByCountry(country) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val mockAttempts: StateFlow<List<MockTestAttempt>> = _selectedCountry
        .flatMapLatest { country -> repository.getMockAttempts(country) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookmarkedQuestions: StateFlow<List<Question>> = _selectedCountry
        .flatMapLatest { country -> repository.getBookmarkedQuestions(country) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val mistakeQuestions: StateFlow<List<Question>> = _selectedCountry
        .flatMapLatest { country -> repository.getMistakeQuestions(country) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun switchCountry(country: Country) {
        _selectedCountry.value = country
        viewModelScope.launch {
            val updated = _userProfile.value.copy(selectedCountry = country.name)
            repository.updateProfile(updated)
            _userProfile.value = updated
        }
        initTutorWelcome()
    }

    fun setPreferredLanguage(langCode: String) {
        _currentLanguage.value = langCode
        viewModelScope.launch {
            val updated = _userProfile.value.copy(preferredLanguage = langCode)
            repository.updateProfile(updated)
            _userProfile.value = updated
        }
    }

    fun completeOnboarding(country: Country, language: String, name: String) {
        viewModelScope.launch {
            val updated = _userProfile.value.copy(
                selectedCountry = country.name,
                preferredLanguage = language,
                userName = name.ifBlank { "Applicant" },
                onboardingCompleted = true,
                lastActiveDateMillis = System.currentTimeMillis()
            )
            repository.updateProfile(updated)
            _selectedCountry.value = country
            _currentLanguage.value = language
            _userProfile.value = updated
            _currentScreen.value = AppScreen.DASHBOARD
        }
    }

    // Quiz Session Management
    fun startMockTest() {
        val country = _selectedCountry.value
        val allQuestionsForCountry = QuestionBank.ALL_QUESTIONS.filter { it.country == country.name }
        val testQuestions = allQuestionsForCountry.shuffled().take(country.totalMockQuestions)
        val timeSecs = country.timeLimitMinutes * 60

        startQuizSession(
            mode = QuizMode.MOCK,
            questions = testQuestions,
            timeSeconds = timeSecs
        )
    }

    fun startQuickQuiz() {
        val country = _selectedCountry.value
        val allQuestionsForCountry = QuestionBank.ALL_QUESTIONS.filter { it.country == country.name }
        val testQuestions = allQuestionsForCountry.shuffled().take(10)

        startQuizSession(
            mode = QuizMode.QUICK_10,
            questions = testQuestions,
            timeSeconds = 0 // Untimed casual mode
        )
    }

    fun startTopicQuiz(topicId: String) {
        val country = _selectedCountry.value
        val topicQuestions = QuestionBank.ALL_QUESTIONS.filter {
            it.country == country.name && it.topicId == topicId
        }
        val questionsToUse = if (topicQuestions.isNotEmpty()) topicQuestions else {
            QuestionBank.ALL_QUESTIONS.filter { it.country == country.name }.take(5)
        }

        startQuizSession(
            mode = QuizMode.TOPIC,
            topicId = topicId,
            questions = questionsToUse,
            timeSeconds = 0
        )
    }

    fun startMistakePractice(mistakes: List<Question>) {
        if (mistakes.isEmpty()) return
        startQuizSession(
            mode = QuizMode.MISTAKES,
            questions = mistakes,
            timeSeconds = 0
        )
    }

    private fun startQuizSession(
        mode: QuizMode,
        topicId: String? = null,
        questions: List<Question>,
        timeSeconds: Int
    ) {
        timerJob?.cancel()
        _quizSession.value = QuizSessionState(
            mode = mode,
            topicId = topicId,
            questions = questions,
            currentIndex = 0,
            userAnswers = emptyMap(),
            flaggedIndexes = emptySet(),
            timeRemainingSeconds = timeSeconds,
            totalTimeSeconds = timeSeconds,
            isTimerActive = timeSeconds > 0,
            isFinished = false
        )
        _currentScreen.value = AppScreen.QUIZ

        if (timeSeconds > 0) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_quizSession.value.timeRemainingSeconds > 0 && !_quizSession.value.isFinished) {
                delay(1000L)
                _quizSession.update { current ->
                    val newRemaining = current.timeRemainingSeconds - 1
                    if (newRemaining <= 0) {
                        current.copy(timeRemainingSeconds = 0, isTimerActive = false)
                    } else {
                        current.copy(timeRemainingSeconds = newRemaining)
                    }
                }
                if (_quizSession.value.timeRemainingSeconds <= 0) {
                    finishQuiz()
                    break
                }
            }
        }
    }

    fun selectOption(optionIndex: Int) {
        val current = _quizSession.value
        if (current.isFinished || current.questions.isEmpty()) return

        val newAnswers = current.userAnswers.toMutableMap()
        newAnswers[current.currentIndex] = optionIndex
        _quizSession.value = current.copy(userAnswers = newAnswers)

        // Record progress in database
        val q = current.questions[current.currentIndex]
        val isCorrect = (optionIndex == q.correctAnswerIndex)
        viewModelScope.launch {
            repository.saveAnswer(q, optionIndex, isCorrect)
        }
    }

    fun toggleFlagCurrentQuestion() {
        val current = _quizSession.value
        val flags = current.flaggedIndexes.toMutableSet()
        if (flags.contains(current.currentIndex)) {
            flags.remove(current.currentIndex)
        } else {
            flags.add(current.currentIndex)
        }
        _quizSession.value = current.copy(flaggedIndexes = flags)
    }

    fun toggleBookmark(question: Question, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(
                questionId = question.id,
                currentBookmarked = isBookmarked,
                country = question.country,
                topicId = question.topicId
            )
        }
    }

    fun goToQuestion(index: Int) {
        val current = _quizSession.value
        if (index in current.questions.indices) {
            _quizSession.value = current.copy(currentIndex = index)
        }
    }

    fun nextQuestion() {
        val current = _quizSession.value
        if (current.currentIndex < current.questions.size - 1) {
            _quizSession.value = current.copy(currentIndex = current.currentIndex + 1)
        }
    }

    fun previousQuestion() {
        val current = _quizSession.value
        if (current.currentIndex > 0) {
            _quizSession.value = current.copy(currentIndex = current.currentIndex - 1)
        }
    }

    fun finishQuiz() {
        timerJob?.cancel()
        val current = _quizSession.value
        val questions = current.questions
        val country = _selectedCountry.value

        var correctCount = 0
        questions.forEachIndexed { index, q ->
            val userSelected = current.userAnswers[index]
            if (userSelected != null && userSelected == q.correctAnswerIndex) {
                correctCount++
            }
        }

        val passed = correctCount >= country.passingScore
        val timeSpent = current.totalTimeSeconds - current.timeRemainingSeconds

        _quizSession.value = current.copy(
            isFinished = true,
            score = correctCount,
            passed = passed,
            timeSpentSeconds = if (current.totalTimeSeconds > 0) timeSpent else 0
        )

        // If mock test, save attempt to DB
        if (current.mode == QuizMode.MOCK) {
            viewModelScope.launch {
                val attempt = MockTestAttempt(
                    country = country.name,
                    score = correctCount,
                    totalQuestions = questions.size,
                    passingScore = country.passingScore,
                    passed = passed,
                    timeSpentSeconds = timeSpent
                )
                repository.recordMockTestAttempt(attempt)
                repository.checkAndUpdateStreak(_userProfile.value)
            }
        }

        _currentScreen.value = AppScreen.QUIZ_RESULT
    }

    // AI Tutor Chat
    fun askTutor(userQuery: String) {
        if (userQuery.isBlank()) return
        val userMsg = TutorMessage(
            sender = TutorMessage.Sender.USER,
            text = userQuery,
            languageCode = _currentLanguage.value
        )
        _tutorMessages.value = _tutorMessages.value + userMsg

        _isTutorThinking.value = true
        viewModelScope.launch {
            delay(500) // Realistic thoughtful brief pause
            val response = tutorService.getTutorResponse(
                query = userQuery,
                country = _selectedCountry.value,
                preferredLanguage = _currentLanguage.value
            )
            _tutorMessages.value = _tutorMessages.value + response
            _isTutorThinking.value = false
        }
    }

    fun resetDataForCurrentCountry() {
        viewModelScope.launch {
            repository.resetCountryData(_selectedCountry.value)
        }
    }
}
