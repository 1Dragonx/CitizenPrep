package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.model.Country
import com.example.ui.theme.CanadaRed
import com.example.ui.theme.CivicNavyPrimary
import com.example.ui.theme.UsaBlue
import com.example.ui.theme.WarningOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenTopBar(
    currentCountry: Country,
    streakDays: Int,
    onCountrySelected: (Country) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCountryMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "CitizenPrep",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            // Country selector badge
            Box {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (currentCountry == Country.CANADA) CanadaRed.copy(alpha = 0.12f) else UsaBlue.copy(alpha = 0.12f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showCountryMenu = true }
                        .testTag("country_switcher_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${currentCountry.flagEmoji} ${currentCountry.displayName}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (currentCountry == Country.CANADA) CanadaRed else UsaBlue
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Switch Country",
                            tint = if (currentCountry == Country.CANADA) CanadaRed else UsaBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showCountryMenu,
                    onDismissRequest = { showCountryMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(Country.CANADA.flagEmoji, modifier = Modifier.padding(end = 8.dp))
                                Text("Canada (Discover Canada)")
                            }
                        },
                        onClick = {
                            onCountrySelected(Country.CANADA)
                            showCountryMenu = false
                        },
                        modifier = Modifier.testTag("select_canada_menu_item")
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(Country.USA.flagEmoji, modifier = Modifier.padding(end = 8.dp))
                                Text("United States (USCIS 2025)")
                            }
                        },
                        onClick = {
                            onCountrySelected(Country.USA)
                            showCountryMenu = false
                        },
                        modifier = Modifier.testTag("select_usa_menu_item")
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Streak indicator
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = WarningOrange.copy(alpha = 0.12f),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = WarningOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${streakDays}d",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarningOrange,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            // Settings button
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.testTag("settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.testTag("citizen_top_bar")
    )
}
