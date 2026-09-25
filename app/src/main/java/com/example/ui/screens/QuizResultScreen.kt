package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.model.Topic
import com.example.ui.QuizSessionState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.UsaBlue
import com.example.ui.theme.WarningOrange

@Composable
fun QuizResultScreen(
    country: Country,
    sessionState: QuizSessionState,
    preferredLanguage: String,
    onRetake: () -> Unit,
    onStudyWeakTopics: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val total = sessionState.questions.size
    val score = sessionState.score
    val accuracy = if (total > 0) ((score.toFloat() / total.toFloat()) * 100).toInt() else 0
    val passed = score >= country.passingScore
    val incorrectCount = total - score

    var resultLanguage by remember { mutableStateOf(preferredLanguage) }
    var filterMistakesOnly by remember { mutableStateOf(false) }

    val countryColor = if (country == Country.CANADA) CanadaRed else UsaBlue

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("quiz_result_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Score Result Hero
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (passed) SuccessGreen.copy(alpha = 0.08f) else CanadaRed.copy(alpha = 0.08f)
                ),
                border = BorderStroke(2.dp, if (passed) SuccessGreen else CanadaRed),
                modifier = Modifier.fillMaxWidth().testTag("result_hero_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (passed) SuccessGreen.copy(alpha = 0.15f) else CanadaRed.copy(alpha = 0.15f),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (passed) Icons.Default.EmojiEvents else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (passed) SuccessGreen else CanadaRed,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (passed) "🎉 Test Passed!" else "Practice Needed",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (passed) SuccessGreen else CanadaRed
                    )

                    Text(
                        text = if (passed) {
                            "Great job! You met the official ${country.displayName} passing threshold (${country.passingScore}/$total)."
                        } else {
                            "You scored $score/$total. Official pass requires at least ${country.passingScore} correct."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$score / $total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                            Text(text = "Final Score", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$accuracy%", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = if (passed) SuccessGreen else WarningOrange)
                            Text(text = "Accuracy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$incorrectCount", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = CanadaRed)
                            Text(text = "Incorrect", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Action Buttons Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onRetake,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = countryColor),
                    modifier = Modifier.weight(1f).testTag("retake_quiz_button")
                ) {
                    Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Try Again")
                }

                OutlinedButton(
                    onClick = onStudyWeakTopics,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).testTag("study_weak_topics_button")
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Study Guide")
                }
            }
        }

        // PRD requirement: Results page ad placement below test result
        item {
            com.example.ui.components.InlineBannerAd(placement = "quiz_results")
        }

        // Review Detailed Questions Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question Review",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                FilterChip(
                    selected = filterMistakesOnly,
                    onClick = { filterMistakesOnly = !filterMistakesOnly },
                    label = { Text("Mistakes Only (${incorrectCount})") }
                )
            }

            // Language switcher for explanations
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("en" to "English", "hi" to "हिन्दी", "fr" to "Français", "es" to "Español").forEach { (code, label) ->
                    FilterChip(
                        selected = resultLanguage == code,
                        onClick = { resultLanguage = code },
                        label = { Text(label, fontSize = 11.sp) }
                    )
                }
            }
        }

        // List of Reviewed Questions
        val displayedQuestions = sessionState.questions.filterIndexed { index, _ ->
            val userAns = sessionState.userAnswers[index]
            val isCorrect = userAns != null && userAns == sessionState.questions[index].correctAnswerIndex
            if (filterMistakesOnly) !isCorrect else true
        }

        itemsIndexed(displayedQuestions) { idx, question ->
            val originalIndex = sessionState.questions.indexOf(question)
            val userSelection = sessionState.userAnswers[originalIndex]
            val isCorrect = userSelection != null && userSelection == question.correctAnswerIndex
            val options = question.getOptions()

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, if (isCorrect) SuccessGreen.copy(alpha = 0.3f) else CanadaRed.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isCorrect) SuccessGreen.copy(alpha = 0.12f) else CanadaRed.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (isCorrect) SuccessGreen else CanadaRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isCorrect) "Correct" else "Incorrect",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrect) SuccessGreen else CanadaRed
                                )
                            }
                        }

                        Text(
                            text = "Q${originalIndex + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = question.questionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Correct answer text
                    Text(
                        text = "Correct Answer: ${options[question.correctAnswerIndex]}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )

                    if (!isCorrect && userSelection != null) {
                        Text(
                            text = "Your Answer: ${options[userSelection]}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CanadaRed
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Multilingual Explanation
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Why? (Explanation)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = question.getExplanation(resultLanguage),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Source: ${question.officialSource}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = onReturnHome,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("return_home_button")
            ) {
                Text("Return to Dashboard")
            }
            Spacer(modifier = Modifier.height(10.dp))
            DisclaimerBanner()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
