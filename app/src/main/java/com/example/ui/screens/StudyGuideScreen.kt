package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Country
import com.example.data.model.StudyLesson
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.CivicNavyPrimary
import com.example.ui.theme.UsaBlue

@Composable
fun StudyGuideScreen(
    country: Country,
    onStartTopicQuiz: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Lessons, 1: Flashcards
    val lessons = StudyLesson.getLessonsFor(country)
    val countryColor = if (country == Country.CANADA) CanadaRed else UsaBlue

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("study_guide_screen")
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${country.flagEmoji} Official Study Guide",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (country == Country.CANADA) "Based on 'Discover Canada' Official Guide" else "Based on USCIS 2025 Civics Curriculum",
            style = MaterialTheme.typography.bodySmall,
            color = countryColor,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Topic Notes (${lessons.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("study_tab_lessons")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Quick Flashcards", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("study_tab_flashcards")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            // Lessons list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(lessons.size) { index ->
                    val lesson = lessons[index]
                    LessonCard(
                        lesson = lesson,
                        countryColor = countryColor,
                        onPracticeTopic = { onStartTopicQuiz(lesson.topicId) }
                    )

                    // PRD requirement: Inline banner ad between lesson content
                    if (index == 1) {
                        Spacer(modifier = Modifier.height(6.dp))
                        com.example.ui.components.InlineBannerAd(placement = "study_lessons")
                    }
                }

                item {
                    DisclaimerBanner()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            // Interactive Flashcards view
            FlashcardsView(lessons = lessons, countryColor = countryColor)
        }
    }
}

@Composable
fun LessonCard(
    lesson: StudyLesson,
    countryColor: androidx.compose.ui.graphics.Color,
    onPracticeTopic: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lesson_card_${lesson.topicId}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = lesson.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = if (expanded) Int.MAX_VALUE else 2
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = countryColor
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    // Key Facts
                    Text(
                        text = "Key Facts to Remember",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = countryColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    lesson.keyFacts.forEach { fact ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("• ", fontWeight = FontWeight.Bold, color = countryColor)
                            Text(fact, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // Key Dates or Numbers
                    if (lesson.importantDatesOrNumbers.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Important Dates & Numbers",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        lesson.importantDatesOrNumbers.forEach { (date, desc) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(date, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = countryColor)
                                Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    // Memory Tip
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CivicGoldSecondary.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = CivicGoldSecondary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tip: ${lesson.memoryTip}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onPracticeTopic,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Practice This Topic Quiz")
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardsView(
    lessons: List<StudyLesson>,
    countryColor: androidx.compose.ui.graphics.Color
) {
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val flashcardItems = remember(lessons) {
        lessons.flatMap { lesson ->
            lesson.keyFacts.map { fact ->
                val parts = fact.split(":", limit = 2)
                val front = if (parts.size > 1) parts[0].trim() else "Key Concept"
                val back = if (parts.size > 1) parts[1].trim() else fact
                Triple(lesson.title, front, back)
            }
        }
    }

    if (flashcardItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No flashcards available.")
        }
        return
    }

    val currentCard = flashcardItems[currentCardIndex.coerceIn(0, flashcardItems.size - 1)]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("flashcards_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Card ${currentCardIndex + 1} of ${flashcardItems.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = currentCard.first,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = countryColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big Flashcard
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isFlipped) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(2.dp, countryColor.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clickable { isFlipped = !isFlipped }
                .testTag("active_flashcard")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = countryColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (isFlipped) "ANSWER / EXPLANATION" else "CONCEPT (TAP TO FLIP)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = countryColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (isFlipped) currentCard.third else currentCard.second,
                        style = if (isFlipped) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.headlineSmall,
                        fontWeight = if (isFlipped) FontWeight.Normal else FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Previous / Next buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    if (currentCardIndex > 0) {
                        currentCardIndex--
                        isFlipped = false
                    }
                },
                enabled = currentCardIndex > 0,
                modifier = Modifier.testTag("flashcard_prev_button")
            ) {
                Text("Previous")
            }

            Button(
                onClick = { isFlipped = !isFlipped },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = countryColor)
            ) {
                Text(if (isFlipped) "Show Question" else "Flip Card")
            }

            OutlinedButton(
                onClick = {
                    if (currentCardIndex < flashcardItems.size - 1) {
                        currentCardIndex++
                        isFlipped = false
                    }
                },
                enabled = currentCardIndex < flashcardItems.size - 1,
                modifier = Modifier.testTag("flashcard_next_button")
            ) {
                Text("Next")
            }
        }
    }
}
