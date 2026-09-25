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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Country
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.CivicNavyPrimary
import com.example.ui.theme.UsaBlue

@Composable
fun OnboardingScreen(
    onComplete: (Country, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCountry by remember { mutableStateOf(Country.CANADA) }
    var selectedLanguage by remember { mutableStateOf("en") }
    var applicantName by remember { mutableStateOf("") }

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
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .testTag("onboarding_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // App Emblem
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(68.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to CitizenPrep",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Prepare smarter for your official citizenship exam with syllabus lessons, timed mock tests, and smart practice.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        Text(
            text = "Which test are you preparing for?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 12.dp)
        )

        // Canada Card
        CountrySelectionCard(
            country = Country.CANADA,
            title = "Canada Citizenship Test",
            subtitle = "Based on official 'Discover Canada' guide",
            testSpecs = "20 Questions • 45 Minutes • 15/20 Passing Target",
            isSelected = selectedCountry == Country.CANADA,
            accentColor = CanadaRed,
            onClick = { selectedCountry = Country.CANADA },
            modifier = Modifier.testTag("select_country_canada")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // USA Card
        CountrySelectionCard(
            country = Country.USA,
            title = "United States Civics Test",
            subtitle = "Based on official USCIS 2025 128-question pool",
            testSpecs = "20 Interview Questions • 12/20 Passing Target",
            isSelected = selectedCountry == Country.USA,
            accentColor = UsaBlue,
            onClick = { selectedCountry = Country.USA },
            modifier = Modifier.testTag("select_country_usa")
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Explanation / Study Language
        Text(
            text = "Preferred Explanation Language",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = "Official questions are tested in standard format; explanations will be available in your chosen language.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            languages.forEach { (code, label) ->
                FilterChip(
                    selected = selectedLanguage == code,
                    onClick = { selectedLanguage = code },
                    label = { Text(label, fontSize = 12.sp) },
                    modifier = Modifier.testTag("lang_chip_$code")
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Applicant Name
        OutlinedTextField(
            value = applicantName,
            onValueChange = { applicantName = it },
            label = { Text("Your Name (Optional)") },
            placeholder = { Text("e.g. Avi") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("applicant_name_input")
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Start Button
        Button(
            onClick = {
                onComplete(selectedCountry, selectedLanguage, applicantName)
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("begin_preparation_button")
        ) {
            Text(
                text = "Begin Preparation →",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        DisclaimerBanner()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CountrySelectionCard(
    country: Country,
    title: String,
    subtitle: String,
    testSpecs: String,
    isSelected: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) accentColor.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = country.flagEmoji,
                fontSize = 36.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = accentColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = testSpecs,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = accentColor,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
