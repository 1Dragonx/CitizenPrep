package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkCivicNavyPrimary,
    onPrimary = DarkCivicNavyOnPrimary,
    primaryContainer = DarkCivicNavyContainer,
    onPrimaryContainer = DarkCivicNavyOnContainer,
    secondary = DarkCivicGoldSecondary,
    onSecondary = DarkCivicGoldOnSecondary,
    secondaryContainer = DarkCivicGoldContainer,
    onSecondaryContainer = DarkCivicGoldOnContainer,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = CivicNavyPrimary,
    onPrimary = CivicNavyOnPrimary,
    primaryContainer = CivicNavyContainer,
    onPrimaryContainer = CivicNavyOnContainer,
    secondary = CivicGoldSecondary,
    onSecondary = CivicGoldOnSecondary,
    secondaryContainer = CivicGoldContainer,
    onSecondaryContainer = CivicGoldOnContainer,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)

@Composable
fun CitizenPrepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
