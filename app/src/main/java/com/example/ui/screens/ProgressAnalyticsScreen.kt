package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Country
import com.example.data.model.MockTestAttempt
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressAnalyticsScreen(
    country: Country,
    userProfile: UserProfile,
    progressList: List<QuestionProgress>,
    mockAttempts: List<MockTestAttempt>,
    modifier: Modifier = Modifier
) {
    val countryColor = if (country == Country.CANADA) CanadaRed else UsaBlue
    val topics = Topic.getTopicsFor(country)

    val totalAttempted = progressList.sumOf { it.timesAnswered }
    val totalCorrect = progressList.sumOf { it.timesCorrect }
    val accuracy = if (totalAttempted > 0) ((totalCorrect.toFloat() / totalAttempted.toFloat()) * 100).toInt() else 0

    // Practice Readiness calculation
    val answeredUniqueCount = progressList.count { it.timesAnswered > 0 }
    val topicCoverage = if (topics.isNotEmpty()) (answeredUniqueCount.toFloat() / (topics.size * 2).toFloat()).coerceIn(0f, 1f) else 0f
    val readiness = ((accuracy * 0.6f) + (topicCoverage * 40f)).toInt().coerceIn(0, 100)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("analytics_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${country.flagEmoji} Performance Analytics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Track your readiness score, accuracy trends, and mock exam history.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Readiness Score Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
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
                                text = "Exam Readiness Indicator",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Target: ${country.passingScore}/${country.totalMockQuestions} passing",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "$readiness%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (readiness >= 75) SuccessGreen else CivicGoldSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { readiness / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = if (readiness >= 75) SuccessGreen else CivicGoldSecondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (readiness >= 75) "✓ High confidence: meeting mock benchmark criteria." else "• Continue daily quizzes to reach 75%+ readiness score.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Summary Statistics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total Answered",
                    value = "$totalAttempted",
                    icon = Icons.Default.Quiz,
                    iconColor = countryColor,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Correct Rate",
                    value = "$accuracy%",
                    icon = Icons.Default.TrendingUp,
                    iconColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Active Streak",
                    value = "${userProfile.streakDays}d",
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = WarningOrange,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Topic Mastery Breakdown
        item {
            Text(
                text = "Topic-Wise Mastery",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(topics) { topic ->
            val topicProgress = progressList.filter { it.topicId == topic.id }
            val attempted = topicProgress.sumOf { it.timesAnswered }
            val correct = topicProgress.sumOf { it.timesCorrect }
            val topicAcc = if (attempted > 0) ((correct.toFloat() / attempted.toFloat()) * 100).toInt() else 0

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
                        Text(
                            text = topic.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (attempted > 0) "$topicAcc% ($correct/$attempted)" else "Not attempted",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (topicAcc >= 75) SuccessGreen else if (attempted > 0) WarningOrange else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { if (attempted > 0) (topicAcc / 100f) else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (topicAcc >= 75) SuccessGreen else if (attempted > 0) WarningOrange else countryColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // Recent Mock Test Attempts History
        item {
            Text(
                text = "Mock Exam History (${mockAttempts.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (mockAttempts.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No mock tests completed yet. Take your first 20-question mock test to establish a benchmark!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(mockAttempts) { attempt ->
                val dateFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(attempt.timestamp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, if (attempt.passed) SuccessGreen.copy(alpha = 0.3f) else CanadaRed.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (attempt.passed) SuccessGreen.copy(alpha = 0.15f) else CanadaRed.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (attempt.passed) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (attempt.passed) SuccessGreen else CanadaRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (attempt.passed) "PASSED (${attempt.score}/${attempt.totalQuestions})" else "NEEDS PRACTICE (${attempt.score}/${attempt.totalQuestions})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (attempt.passed) SuccessGreen else CanadaRed
                            )
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            val acc = ((attempt.score.toFloat() / attempt.totalQuestions.toFloat()) * 100).toInt()
                            Text(
                                text = "$acc%",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
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
