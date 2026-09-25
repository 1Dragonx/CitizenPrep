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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.Country
import com.example.data.model.UserProfile
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.UsaBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentCountry: Country,
    userProfile: UserProfile,
    onBack: () -> Unit,
    onCountrySwitch: (Country) -> Unit,
    onLanguageChange: (String) -> Unit,
    onUpdateProfile: (UserProfile) -> Unit,
    onResetData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editName by remember { mutableStateOf(userProfile.userName) }
    var selectedGoal by remember { mutableIntStateOf(userProfile.dailyGoalQuestions) }
    var showResetDialog by remember { mutableStateOf(false) }
    var adsEnabled by remember { mutableStateOf(com.example.ui.components.AdManager.isAdsEnabled) }

    val languages = listOf(
        "en" to "English",
        "hi" to "हिन्दी (Hindi)",
        "fr" to "Français",
        "es" to "Español"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen")
    ) {
        TopAppBar(
            title = { Text("Settings & Profile", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Target Country Switcher Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Active Citizenship Exam", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Canada Option
                            CountryOptionTile(
                                country = Country.CANADA,
                                isSelected = currentCountry == Country.CANADA,
                                accentColor = CanadaRed,
                                onClick = { onCountrySwitch(Country.CANADA) },
                                modifier = Modifier.weight(1f)
                            )

                            // USA Option
                            CountryOptionTile(
                                country = Country.USA,
                                isSelected = currentCountry == Country.USA,
                                accentColor = UsaBlue,
                                onClick = { onCountrySwitch(Country.USA) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Language Preference Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Explanation & Tutor Language", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            languages.forEach { (code, label) ->
                                FilterChip(
                                    selected = userProfile.preferredLanguage == code,
                                    onClick = { onLanguageChange(code) },
                                    label = { Text(label, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            // User Profile & Daily Goal Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Applicant Profile & Study Plan", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = editName,
                            onValueChange = {
                                editName = it
                                onUpdateProfile(userProfile.copy(userName = it))
                            },
                            label = { Text("Display Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Daily Practice Goal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(10, 15, 20, 30).forEach { goal ->
                                FilterChip(
                                    selected = selectedGoal == goal,
                                    onClick = {
                                        selectedGoal = goal
                                        onUpdateProfile(userProfile.copy(dailyGoalQuestions = goal))
                                    },
                                    label = { Text("$goal Qs") }
                                )
                            }
                        }
                    }
                }
            }

            // Monetization & Inline Banner Ads Control (PRD Requirement)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth().testTag("ad_settings_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Inline Banner Advertisements",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Non-intrusive ads in lessons, quizzes, & results",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Switch(
                                checked = adsEnabled,
                                onCheckedChange = {
                                    adsEnabled = it
                                    com.example.ui.components.AdManager.isAdsEnabled = it
                                },
                                modifier = Modifier.testTag("toggle_ads_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Ad Unit ID: ${com.example.ui.components.AdManager.TEST_BANNER_AD_UNIT_ID}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Impressions: ${com.example.ui.components.AdManager.totalImpressions}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "Clicks: ${com.example.ui.components.AdManager.totalClicks}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "CTR: ${String.format(java.util.Locale.US, "%.1f", com.example.ui.components.AdManager.getCtr())}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Official Study Sources & Citations
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Official Sources of Truth", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "🇨🇦 Canada:\nOfficial Study Guide: 'Discover Canada: The Rights and Responsibilities of Citizenship', published by Immigration, Refugees and Citizenship Canada (IRCC). Format: 20 Questions, 45 Minutes, 15/20 Passing Score.\n\n🇺🇸 United States:\nOfficial 2025 Civics Naturalization Test (128 Question Pool M-1778), published by USCIS. Format: 20 Oral/Test Questions, 12/20 Passing Target.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Reset Data Button
            item {
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        contentColor = CanadaRed
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("reset_progress_button")
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset ${currentCountry.displayName} Quiz Progress")
                }
            }

            item {
                DisclaimerBanner()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress?") },
            text = { Text("This will clear your question answers, streak and mock exam attempts for ${currentCountry.displayName}. This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetData()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CanadaRed)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CountryOptionTile(
    country: Country,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(country.flagEmoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(country.displayName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedButton(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isSelected) "Active" else "Switch", fontSize = 11.sp)
            }
        }
    }
}
