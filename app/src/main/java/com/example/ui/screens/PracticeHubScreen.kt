package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Country
import com.example.data.model.Question
import com.example.data.model.Topic
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.CivicNavyPrimary
import com.example.ui.theme.UsaBlue
import com.example.ui.theme.WarningOrange

@Composable
fun PracticeHubScreen(
    country: Country,
    mistakeQuestions: List<Question>,
    bookmarkedQuestions: List<Question>,
    onStartMockTest: () -> Unit,
    onStartQuickQuiz: () -> Unit,
    onStartTopicQuiz: (String) -> Unit,
    onStartMistakesQuiz: () -> Unit,
    onStartBookmarksQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val countryColor = if (country == Country.CANADA) CanadaRed else UsaBlue
    val topics = Topic.getTopicsFor(country)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("practice_hub_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${country.flagEmoji} ${country.displayName} Practice Center",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Train with official question formats, timed simulations, and targeted review.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Full Official Mock Test
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = countryColor.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.5.dp, countryColor.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hub_mock_test_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = countryColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "OFFICIAL EXAM SIMULATION",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = countryColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = countryColor
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Full Mock Citizenship Test",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (country == Country.CANADA) {
                            "20 official-style questions • 45 minutes countdown • Passing score: 15/20 (75%). Mirrors real Discover Canada online test."
                        } else {
                            "20 civics questions • Timed interview practice • Passing target: 12/20 (60%). Based on official USCIS 2025 pool."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                    )

                    Button(
                        onClick = onStartMockTest,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("hub_start_mock_button")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Mock Test Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Modes
        item {
            Text(
                text = "Rapid Training",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PracticeModeCard(
                    title = "Quick 10 Quiz",
                    subtitle = "10 random questions",
                    icon = Icons.Default.Quiz,
                    iconTint = countryColor,
                    badge = "Casual",
                    onClick = onStartQuickQuiz,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("mode_quick_10")
                )

                PracticeModeCard(
                    title = "Review Mistakes",
                    subtitle = "${mistakeQuestions.size} missed questions",
                    icon = Icons.Default.Warning,
                    iconTint = WarningOrange,
                    badge = if (mistakeQuestions.isNotEmpty()) "${mistakeQuestions.size} Ready" else "0",
                    enabled = mistakeQuestions.isNotEmpty(),
                    onClick = onStartMistakesQuiz,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("mode_review_mistakes")
                )
            }
        }

        item {
            PracticeModeCard(
                title = "Bookmarked Questions",
                subtitle = "${bookmarkedQuestions.size} questions saved for review",
                icon = Icons.Default.Bookmark,
                iconTint = CivicGoldSecondary,
                badge = "${bookmarkedQuestions.size} Saved",
                enabled = bookmarkedQuestions.isNotEmpty(),
                onClick = onStartBookmarksQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mode_bookmarked")
            )
        }

        // Topic Quizzes Section
        item {
            Text(
                text = "Topic-Wise Quizzes (${topics.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Master specific sections of the official syllabus.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(topics.size) { index ->
            val topic = topics[index]
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartTopicQuiz(topic.id) }
                    .testTag("hub_topic_${topic.id}")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = countryColor.copy(alpha = 0.12f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("${index + 1}", fontWeight = FontWeight.Bold, color = countryColor)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

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

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Start Topic",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
fun PracticeModeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    badge: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = modifier.clickable(enabled = enabled, onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = if (enabled) iconTint else MaterialTheme.colorScheme.outline)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
