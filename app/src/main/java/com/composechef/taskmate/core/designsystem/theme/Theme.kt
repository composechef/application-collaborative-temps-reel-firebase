package com.composechef.taskmate.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors =
    lightColorScheme(
        primary = Indigo,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFEEEDFF),
        onPrimaryContainer = Ink,
        secondary = Color(0xFF4E7551),
        secondaryContainer = Color(0xFFEEEDFF),
        onSecondaryContainer = Ink,
        background = Canvas,
        surface = Canvas,
        onBackground = Ink,
        onSurface = Ink,
        onSurfaceVariant = Muted,
        surfaceVariant = Color(0xFFF0EFEC),
        surfaceContainerLowest = Color.White,
        surfaceContainerLow = Color(0xFFF5F4F1),
        surfaceContainer = Color(0xFFF1F0ED),
        surfaceContainerHigh = Color(0xFFEBEAE7),
        surfaceContainerHighest = Color(0xFFE5E4E1),
        outline = Color(0xFF898994),
        outlineVariant = Color(0xFFE5E4E9),
    )

/**
 * Chaque rôle est défini explicitement : sinon, Material 3 complète avec sa palette de base,
 * teintée de violet, qui ne s'accorde pas avec les gris neutres de l'app.
 */
private val DarkColors =
    darkColorScheme(
        primary = Color(0xFFBEBBFF),
        onPrimary = Color(0xFF26246B),
        primaryContainer = Color(0xFF35335D),
        onPrimaryContainer = Color(0xFFE3E0FF),
        secondary = Color(0xFFA5D0A8),
        onSecondary = Color(0xFF12361B),
        secondaryContainer = Color(0xFF35335D),
        onSecondaryContainer = Color(0xFFE3E0FF),
        background = Night,
        surface = Night,
        onBackground = Mist,
        onSurface = Mist,
        onSurfaceVariant = Color(0xFFA9A9B5),
        surfaceVariant = Color(0xFF2B2B33),
        surfaceContainerLowest = Color(0xFF141419),
        surfaceContainerLow = Color(0xFF212128),
        surfaceContainer = Color(0xFF25252D),
        surfaceContainerHigh = Color(0xFF2C2C35),
        surfaceContainerHighest = Color(0xFF35353F),
        outline = Color(0xFF8C8C98),
        outlineVariant = Color(0xFF3A3A44),
    )

private val LocalListColors = staticCompositionLocalOf { LightListColors }

/** Les couleurs de la liste d'index [index], adaptées au thème clair ou sombre. */
@Composable
@ReadOnlyComposable
fun listColor(index: Int): ListColor {
    val colors = LocalListColors.current
    return colors[index.mod(colors.size)]
}

@Composable
fun TaskMateTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalListColors provides if (darkTheme) DarkListColors else LightListColors
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = Typography,
            shapes =
                Shapes(
                    small = RoundedCornerShape(12.dp),
                    medium = RoundedCornerShape(18.dp),
                    large = RoundedCornerShape(26.dp),
                ),
            content = content,
        )
    }
}
