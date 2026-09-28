package com.example.studyplanner.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDefault,
    secondary = AccentStudy,
    tertiary = AccentRevision,
    background = SurfaceBase,
    surface = SurfaceContainerLow,
    onPrimary = OnSurfaceHigh,
    onSecondary = OnSurfaceHigh,
    onTertiary = OnSurfaceHigh,
    onBackground = OnSurfaceHigh,
    onSurface = OnSurfaceHigh,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnSurfaceMedium
)

@Composable
fun StudyPlannerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Always dark as per Luminous AMOLED Expressive

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
