package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.AppScreen

@Composable
fun BottomNavBar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("bottom_nav_bar")
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.DASHBOARD,
            onClick = { onScreenSelected(AppScreen.DASHBOARD) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            modifier = Modifier.testTag("nav_dashboard")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.PRACTICE_HUB,
            onClick = { onScreenSelected(AppScreen.PRACTICE_HUB) },
            icon = { Icon(Icons.Default.Quiz, contentDescription = "Practice") },
            label = { Text("Practice") },
            modifier = Modifier.testTag("nav_practice")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.STUDY_GUIDE,
            onClick = { onScreenSelected(AppScreen.STUDY_GUIDE) },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Study Guide") },
            label = { Text("Study") },
            modifier = Modifier.testTag("nav_study")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.AI_TUTOR,
            onClick = { onScreenSelected(AppScreen.AI_TUTOR) },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Tutor") },
            label = { Text("AI Tutor") },
            modifier = Modifier.testTag("nav_ai_tutor")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.ANALYTICS,
            onClick = { onScreenSelected(AppScreen.ANALYTICS) },
            icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Analytics") },
            label = { Text("Stats") },
            modifier = Modifier.testTag("nav_analytics")
        )
    }
}
