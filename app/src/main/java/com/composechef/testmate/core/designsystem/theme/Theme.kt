package com.composechef.testmate.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors =
    lightColorScheme(
        primary = Indigo,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFEEEDFF),
        onPrimaryContainer = Ink,
        secondary = Color(0xFF4E7551),
        background = Canvas,
        surface = Canvas,
        onBackground = Ink,
        onSurface = Ink,
        onSurfaceVariant = Muted,
        surfaceVariant = Color(0xFFF0EFEC),
        surfaceContainer = Color(0xFFF1F0ED),
        outline = Color(0xFF898994),
        outlineVariant = Color(0xFFE5E4E9),
    )
private val DarkColors =
    darkColorScheme(
        primary = Color(0xFFBEBBFF),
        primaryContainer = Color(0xFF35335D),
        background = Color(0xFF19191F),
        surface = Color(0xFF19191F),
        surfaceContainer = Color(0xFF25252D),
    )

@Composable
fun TaskMateTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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
