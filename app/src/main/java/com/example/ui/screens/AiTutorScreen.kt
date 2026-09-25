package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.TutorMessage
import com.example.data.model.Country
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.CivicNavyPrimary
import com.example.ui.theme.UsaBlue

@Composable
fun AiTutorScreen(
    country: Country,
    messages: List<TutorMessage>,
    isThinking: Boolean,
    preferredLanguage: String,
    onLanguageChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val countryColor = if (country == Country.CANADA) CanadaRed else UsaBlue

    val quickQuestions = if (country == Country.CANADA) {
        listOf(
            "Canadian Parliament मुझे Hindi में समझाओ",
            "What is Habeas Corpus in Canada?",
            "Who are Canada's three founding peoples?",
            "What happened at Vimy Ridge in 1917?"
        )
    } else {
        listOf(
            "US Government की 3 शाखाएं Hindi में बताओ",
            "What does the First Amendment protect?",
            "Why does the US flag have 13 stripes and 50 stars?",
            "What is the system of Checks and Balances?"
        )
    }

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("ai_tutor_screen")
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = CivicGoldSecondary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = CivicGoldSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI Civics Study Tutor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${country.flagEmoji} ${country.displayName} Civics Guide",
                            style = MaterialTheme.typography.labelSmall,
                            color = countryColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Language Selector
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("en" to "EN", "hi" to "हिन्दी", "fr" to "FR").forEach { (code, label) ->
                        FilterChip(
                            selected = preferredLanguage == code,
                            onClick = { onLanguageChange(code) },
                            label = { Text(label, fontSize = 10.sp) },
                            modifier = Modifier.testTag("tutor_lang_$code")
                        )
                    }
                }
            }
        }

        // Quick Suggestion Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickQuestions.take(2).forEach { prompt ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSendMessage(prompt) }
                        .testTag("quick_prompt_chip")
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Chat Message History
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message, countryColor = countryColor)
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Consulting citizenship curriculum...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                DisclaimerBanner()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Input Field Bar
        Surface(
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask any question in English, Hindi, or French...", fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tutor_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(countryColor, shape = CircleShape)
                        .testTag("tutor_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: TutorMessage,
    countryColor: Color
) {
    val isUser = message.sender == TutorMessage.Sender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = CivicGoldSecondary.copy(alpha = 0.2f),
                modifier = Modifier
                    .size(28.dp)
                    .padding(top = 4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CivicGoldSecondary, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) countryColor else MaterialTheme.colorScheme.surface
            ),
            border = if (!isUser) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)) else null,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                )

                if (!isUser && message.officialReference != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Source: ${message.officialReference}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = countryColor.copy(alpha = 0.2f),
                modifier = Modifier
                    .size(28.dp)
                    .padding(top = 4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = countryColor, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
