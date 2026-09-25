package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.OutlinedFlag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Country
import com.example.data.model.Question
import com.example.ui.QuizMode
import com.example.ui.QuizSessionState
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.CivicNavyPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.UsaBlue
import com.example.ui.theme.WarningOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    country: Country,
    sessionState: QuizSessionState,
    preferredLanguage: String,
    onSelectOption: (Int) -> Unit,
    onToggleFlag: () -> Unit,
    onToggleBookmark: (Question, Boolean) -> Unit,
    onGoToQuestion: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPreviousQuestion: () -> Unit,
    onFinishQuiz: () -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (sessionState.questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions available for this session.")
        }
        return
    }

    val currentQuestion = sessionState.questions[sessionState.currentIndex]
    val selectedOption = sessionState.userAnswers[sessionState.currentIndex]
    val isFlagged = sessionState.flaggedIndexes.contains(sessionState.currentIndex)
    val isMock = sessionState.mode == QuizMode.MOCK

    var showSubmitDialog by remember { mutableStateOf(false) }
    var showPaletteSheet by remember { mutableStateOf(false) }
    var explanationLang by remember { mutableStateOf(preferredLanguage) }

    val countryColor = if (country == Country.CANADA) CanadaRed else UsaBlue

    // Format timer
    val minutes = sessionState.timeRemainingSeconds / 60
    val seconds = sessionState.timeRemainingSeconds % 60
    val timerText = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("quiz_screen")
    ) {
        // Quiz Top Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = if (isMock) "Official Mock Test" else "Practice Quiz",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Question ${sessionState.currentIndex + 1} of ${sessionState.questions.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onExitQuiz,
                    modifier = Modifier.testTag("quiz_exit_button")
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Exit Quiz")
                }
            },
            actions = {
                // Countdown timer if Mock mode
                if (sessionState.isTimerActive) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (sessionState.timeRemainingSeconds < 300) WarningOrange.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = if (sessionState.timeRemainingSeconds < 300) WarningOrange else countryColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timerText,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (sessionState.timeRemainingSeconds < 300) WarningOrange else countryColor
                            )
                        }
                    }
                }

                // Flag question
                IconButton(
                    onClick = onToggleFlag,
                    modifier = Modifier.testTag("flag_question_button")
                ) {
                    Icon(
                        imageVector = if (isFlagged) Icons.Default.Flag else Icons.Default.OutlinedFlag,
                        contentDescription = "Flag Question",
                        tint = if (isFlagged) WarningOrange else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Question Palette button
                IconButton(
                    onClick = { showPaletteSheet = true },
                    modifier = Modifier.testTag("open_palette_button")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${sessionState.userAnswers.size}/${sessionState.questions.size}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Progress line
        LinearProgressIndicator(
            progress = { (sessionState.currentIndex + 1).toFloat() / sessionState.questions.size.toFloat() },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = countryColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Scrollable Question and Options Area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Source citation chip
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = currentQuestion.officialSource,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question Text
            Text(
                text = currentQuestion.questionText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("question_text")
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Answer Options A, B, C, D
            val options = currentQuestion.getOptions()
            options.forEachIndexed { index, optionText ->
                val isSelected = selectedOption == index
                val isAnsweredInCasual = !isMock && selectedOption != null
                val isCorrectAnswer = index == currentQuestion.correctAnswerIndex

                val borderColor = when {
                    isAnsweredInCasual && isCorrectAnswer -> SuccessGreen
                    isAnsweredInCasual && isSelected && !isCorrectAnswer -> CanadaRed
                    isSelected -> countryColor
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                }

                val bgColor = when {
                    isAnsweredInCasual && isCorrectAnswer -> SuccessGreen.copy(alpha = 0.1f)
                    isAnsweredInCasual && isSelected && !isCorrectAnswer -> CanadaRed.copy(alpha = 0.1f)
                    isSelected -> countryColor.copy(alpha = 0.08f)
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    border = BorderStroke(if (isSelected || (isAnsweredInCasual && isCorrectAnswer)) 2.dp else 1.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable { onSelectOption(index) }
                        .testTag("option_$index")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) countryColor else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val letter = ('A' + index).toString()
                                Text(
                                    text = letter,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Casual Mode: Show immediate explanation upon selection
            if (!isMock && selectedOption != null) {
                Spacer(modifier = Modifier.height(16.dp))
                val isCorrect = selectedOption == currentQuestion.correctAnswerIndex

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) SuccessGreen.copy(alpha = 0.08f) else CanadaRed.copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(1.dp, if (isCorrect) SuccessGreen.copy(alpha = 0.3f) else CanadaRed.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth().testTag("casual_explanation_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isCorrect) SuccessGreen else CanadaRed
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCorrect) "Correct!" else "Incorrect. Correct: ${options[currentQuestion.correctAnswerIndex]}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) SuccessGreen else CanadaRed
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Multilingual explanation toggles
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("en" to "EN", "hi" to "हिन्दी", "fr" to "FR", "es" to "ES").forEach { (code, label) ->
                                FilterChip(
                                    selected = explanationLang == code,
                                    onClick = { explanationLang = code },
                                    label = { Text(label, fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentQuestion.getExplanation(explanationLang),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // PRD requirement: Non-intrusive inline banner ad at question intervals
            if ((sessionState.currentIndex + 1) % 4 == 0 || (!isMock && selectedOption != null)) {
                Spacer(modifier = Modifier.height(10.dp))
                com.example.ui.components.InlineBannerAd(placement = "quiz_interval")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Navigation Bar
        Surface(
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPreviousQuestion,
                    enabled = sessionState.currentIndex > 0,
                    modifier = Modifier.testTag("quiz_prev_button")
                ) {
                    Text("Previous")
                }

                if (sessionState.currentIndex < sessionState.questions.size - 1) {
                    Button(
                        onClick = onNextQuestion,
                        colors = ButtonDefaults.buttonColors(containerColor = countryColor),
                        modifier = Modifier.testTag("quiz_next_button")
                    ) {
                        Text("Next")
                    }
                } else {
                    Button(
                        onClick = { showSubmitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.testTag("quiz_submit_button")
                    ) {
                        Text("Finish Test")
                    }
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitDialog) {
        val answeredCount = sessionState.userAnswers.size
        val totalCount = sessionState.questions.size
        val unansweredCount = totalCount - answeredCount

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Submit Exam?") },
            text = {
                Column {
                    Text("You have answered $answeredCount of $totalCount questions.")
                    if (unansweredCount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Warning: $unansweredCount question(s) are still unanswered.",
                            color = WarningOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        onFinishQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    modifier = Modifier.testTag("confirm_submit_button")
                ) {
                    Text("Submit Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text("Keep Practicing")
                }
            }
        )
    }

    // Question Palette Bottom Sheet
    if (showPaletteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaletteSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .testTag("palette_sheet")
            ) {
                Text(
                    text = "Question Palette",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap any question to jump directly to it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sessionState.questions.size) { idx ->
                        val isAnswered = sessionState.userAnswers.containsKey(idx)
                        val isCurrent = sessionState.currentIndex == idx
                        val isItemFlagged = sessionState.flaggedIndexes.contains(idx)

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isCurrent -> countryColor
                                isAnswered -> countryColor.copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            border = if (isItemFlagged) BorderStroke(2.dp, WarningOrange) else null,
                            modifier = Modifier
                                .height(44.dp)
                                .clickable {
                                    onGoToQuestion(idx)
                                    showPaletteSheet = false
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${idx + 1}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        showPaletteSheet = false
                        showSubmitDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finish and Score")
                }
            }
        }
    }
}
