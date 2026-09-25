package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Country
import com.example.ui.AppScreen
import com.example.ui.CitizenPrepViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CitizenTopBar
import com.example.ui.screens.AiTutorScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PracticeHubScreen
import com.example.ui.screens.ProgressAnalyticsScreen
import com.example.ui.screens.QuizResultScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudyGuideScreen
import com.example.ui.theme.CitizenPrepTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CitizenPrepViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CitizenPrepTheme {
                CitizenPrepApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CitizenPrepApp(viewModel: CitizenPrepViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedCountry by viewModel.selectedCountry.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val quizSession by viewModel.quizSession.collectAsStateWithLifecycle()
    val tutorMessages by viewModel.tutorMessages.collectAsStateWithLifecycle()
    val isTutorThinking by viewModel.isTutorThinking.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()

    val progressList by viewModel.currentProgress.collectAsStateWithLifecycle()
    val mockAttempts by viewModel.mockAttempts.collectAsStateWithLifecycle()
    val mistakeQuestions by viewModel.mistakeQuestions.collectAsStateWithLifecycle()
    val bookmarkedQuestions by viewModel.bookmarkedQuestions.collectAsStateWithLifecycle()

    // Back handling for sub-screens per Android guidelines
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD && currentScreen != AppScreen.ONBOARDING) {
        when (currentScreen) {
            AppScreen.QUIZ -> viewModel.navigateTo(AppScreen.PRACTICE_HUB)
            AppScreen.QUIZ_RESULT -> viewModel.navigateTo(AppScreen.DASHBOARD)
            AppScreen.SETTINGS -> viewModel.navigateTo(AppScreen.DASHBOARD)
            else -> viewModel.navigateTo(AppScreen.DASHBOARD)
        }
    }

    if (currentScreen == AppScreen.ONBOARDING) {
        OnboardingScreen(
            onComplete = { country, lang, name ->
                viewModel.completeOnboarding(country, lang, name)
            }
        )
        return
    }

    val showTopBar = currentScreen in listOf(
        AppScreen.DASHBOARD,
        AppScreen.PRACTICE_HUB,
        AppScreen.STUDY_GUIDE,
        AppScreen.ANALYTICS
    )

    val showBottomNav = currentScreen in listOf(
        AppScreen.DASHBOARD,
        AppScreen.PRACTICE_HUB,
        AppScreen.STUDY_GUIDE,
        AppScreen.AI_TUTOR,
        AppScreen.ANALYTICS
    )

    Scaffold(
        topBar = {
            if (showTopBar) {
                CitizenTopBar(
                    currentCountry = selectedCountry,
                    streakDays = userProfile.streakDays,
                    onCountrySelected = { viewModel.switchCountry(it) },
                    onSettingsClick = { viewModel.navigateTo(AppScreen.SETTINGS) }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { viewModel.navigateTo(it) }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.DASHBOARD -> {
                    DashboardScreen(
                        country = selectedCountry,
                        userProfile = userProfile,
                        progressList = progressList,
                        mistakeQuestions = mistakeQuestions,
                        onStartMockTest = { viewModel.startMockTest() },
                        onStartQuickQuiz = { viewModel.startQuickQuiz() },
                        onStartTopicQuiz = { topicId -> viewModel.startTopicQuiz(topicId) },
                        onPracticeMistakes = { viewModel.startMistakePractice(mistakeQuestions) },
                        onOpenStudyGuide = { viewModel.navigateTo(AppScreen.STUDY_GUIDE) },
                        onOpenAiTutor = { viewModel.navigateTo(AppScreen.AI_TUTOR) }
                    )
                }

                AppScreen.PRACTICE_HUB -> {
                    PracticeHubScreen(
                        country = selectedCountry,
                        mistakeQuestions = mistakeQuestions,
                        bookmarkedQuestions = bookmarkedQuestions,
                        onStartMockTest = { viewModel.startMockTest() },
                        onStartQuickQuiz = { viewModel.startQuickQuiz() },
                        onStartTopicQuiz = { topicId -> viewModel.startTopicQuiz(topicId) },
                        onStartMistakesQuiz = { viewModel.startMistakePractice(mistakeQuestions) },
                        onStartBookmarksQuiz = { viewModel.startMistakePractice(bookmarkedQuestions) }
                    )
                }

                AppScreen.STUDY_GUIDE -> {
                    StudyGuideScreen(
                        country = selectedCountry,
                        onStartTopicQuiz = { topicId -> viewModel.startTopicQuiz(topicId) }
                    )
                }

                AppScreen.QUIZ -> {
                    QuizScreen(
                        country = selectedCountry,
                        sessionState = quizSession,
                        preferredLanguage = currentLanguage,
                        onSelectOption = { viewModel.selectOption(it) },
                        onToggleFlag = { viewModel.toggleFlagCurrentQuestion() },
                        onToggleBookmark = { q, isBookmarked -> viewModel.toggleBookmark(q, isBookmarked) },
                        onGoToQuestion = { viewModel.goToQuestion(it) },
                        onNextQuestion = { viewModel.nextQuestion() },
                        onPreviousQuestion = { viewModel.previousQuestion() },
                        onFinishQuiz = { viewModel.finishQuiz() },
                        onExitQuiz = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                    )
                }

                AppScreen.QUIZ_RESULT -> {
                    QuizResultScreen(
                        country = selectedCountry,
                        sessionState = quizSession,
                        preferredLanguage = currentLanguage,
                        onRetake = {
                            if (quizSession.mode == com.example.ui.QuizMode.MOCK) {
                                viewModel.startMockTest()
                            } else {
                                viewModel.startQuickQuiz()
                            }
                        },
                        onStudyWeakTopics = { viewModel.navigateTo(AppScreen.STUDY_GUIDE) },
                        onReturnHome = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                    )
                }

                AppScreen.AI_TUTOR -> {
                    AiTutorScreen(
                        country = selectedCountry,
                        messages = tutorMessages,
                        isThinking = isTutorThinking,
                        preferredLanguage = currentLanguage,
                        onLanguageChange = { viewModel.setPreferredLanguage(it) },
                        onSendMessage = { viewModel.askTutor(it) }
                    )
                }

                AppScreen.ANALYTICS -> {
                    ProgressAnalyticsScreen(
                        country = selectedCountry,
                        userProfile = userProfile,
                        progressList = progressList,
                        mockAttempts = mockAttempts
                    )
                }

                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        currentCountry = selectedCountry,
                        userProfile = userProfile,
                        onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        onCountrySwitch = { viewModel.switchCountry(it) },
                        onLanguageChange = { viewModel.setPreferredLanguage(it) },
                        onUpdateProfile = { viewModel.completeOnboarding(selectedCountry, it.preferredLanguage, it.userName) },
                        onResetData = { viewModel.resetDataForCurrentCountry() }
                    )
                }

                AppScreen.ONBOARDING -> {
                    // Handled above
                }
            }
        }
    }
}
