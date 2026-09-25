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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Country
import com.example.data.model.Question
import com.example.data.model.QuestionProgress
import com.example.data.model.Topic
import com.example.data.model.UserProfile
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.CivicNavyPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.UsaBlue
import com.example.ui.theme.WarningOrange

@Composable
fun DashboardScreen(
    country: Country,
    userProfile: UserProfile,
    progressList: List<QuestionProgress>,
    mistakeQuestions: List<Question>,
    onStartMockTest: () -> Unit,
    onStartQuickQuiz: () -> Unit,
    onStartTopicQuiz: (String) -> Unit,
    onPracticeMistakes: () -> Unit,
    onOpenStudyGuide: () -> Unit,
    onOpenAiTutor: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topics = Topic.getTopicsFor(country)

    // Calculate metrics
    val totalAnswered = progressList.sumOf { it.timesAnswered }
    val totalCorrect = progressList.sumOf { it.timesCorrect }
    val accuracy = if (totalAnswered > 0) ((totalCorrect.toFloat() / totalAnswered.toFloat()) * 100).toInt() else 0

    // Practice readiness indicator formula
    val answeredQuestionsCount = progressList.count { it.timesAnswered > 0 }
    val estimatedTopicCoverage = if (topics.isNotEmpty()) (answeredQuestionsCount.toFloat() / (topics.size * 2).toFloat()).coerceIn(0f, 1f) else 0f
    val readinessScore = ((accuracy * 0.6f) + (estimatedTopicCoverage * 40f)).toInt().coerceIn(0, 100)

    val countryColor = if (country == Country.CANADA) CanadaRed else UsaBlue

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Greeting & Header Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = countryColor.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, countryColor.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Welcome, ${userProfile.userName} 👋",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${country.flagEmoji} ${country.displayName} Citizenship Prep",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = countryColor
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = countryColor.copy(alpha = 0.15f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(country.flagEmoji, fontSize = 24.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Readiness Score Gauge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Practice Readiness",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "(Indicator)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = if (readinessScore >= 75) "On track for passing target!" else "Continue practice to build confidence",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "$readinessScore%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (readinessScore >= 75) SuccessGreen else CivicGoldSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { readinessScore / 100f },
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = if (readinessScore >= 75) SuccessGreen else CivicGoldSecondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // Stats Row (Streak, Answered, Accuracy)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Streak",
                    value = "${userProfile.streakDays} Days",
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = WarningOrange,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Answered",
                    value = "$totalAnswered",
                    icon = Icons.Default.Quiz,
                    iconColor = CivicNavyPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Accuracy",
                    value = "$accuracy%",
                    icon = Icons.Default.CheckCircle,
                    iconColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Primary Action Cards
        item {
            Text(
                text = "Preparation Modes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            // Official Mock Exam Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("official_mock_test_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Official Format Mock Exam",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${country.totalMockQuestions} Questions • ${country.timeLimitMinutes} Mins • Target: ${country.passingScore}/${country.totalMockQuestions}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onStartMockTest,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("start_mock_test_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Start Mock Test")
                        }
                    }
                }
            }
        }

        // Quick Quiz & Mistake Review row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Quiz, contentDescription = null, tint = countryColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Quick 10 Quiz", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text("10 random questions", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = onStartQuickQuiz,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_quick_quiz_button")
                        ) {
                            Text("Practice")
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = WarningOrange)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Review Mistakes", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = if (mistakeQuestions.isNotEmpty()) "${mistakeQuestions.size} to review" else "None currently",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = onPracticeMistakes,
                            enabled = mistakeQuestions.isNotEmpty(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_mistakes_button")
                        ) {
                            Text("Review")
                        }
                    }
                }
            }
        }

        // AI Tutor Highlight
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CivicGoldSecondary.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, CivicGoldSecondary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = CivicGoldSecondary.copy(alpha = 0.15f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CivicGoldSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("AI Civics Tutor", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text("Explain Parliament, Rights & History in English, Hindi, or French", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(
                        onClick = onOpenAiTutor,
                        colors = ButtonDefaults.buttonColors(containerColor = CivicGoldSecondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("open_ai_tutor_button")
                    ) {
                        Text("Ask", color = Color.White)
                    }
                }
            }
        }

        // Topic Mastery Breakdown
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Topic Syllabus (${topics.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onOpenStudyGuide,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("view_full_study_guide_button")
                ) {
                    Text("Study Guide", fontSize = 12.sp)
                }
            }
        }

        items(topics) { topic ->
            val topicProgress = progressList.filter { it.topicId == topic.id }
            val attempted = topicProgress.sumOf { it.timesAnswered }
            val correct = topicProgress.sumOf { it.timesCorrect }
            val topicAccuracy = if (attempted > 0) ((correct.toFloat() / attempted.toFloat()) * 100).toInt() else 0

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = topic.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = topic.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }

                        OutlinedButton(
                            onClick = { onStartTopicQuiz(topic.id) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("practice_topic_${topic.id}")
                        ) {
                            Text("Quiz", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LinearProgressIndicator(
                            progress = { if (attempted > 0) (topicAccuracy / 100f) else 0f },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (topicAccuracy >= 75) SuccessGreen else countryColor,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (attempted > 0) "$topicAccuracy%" else "New",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (attempted > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            DisclaimerBanner()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
