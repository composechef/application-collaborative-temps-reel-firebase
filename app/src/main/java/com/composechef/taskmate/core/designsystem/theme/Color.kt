package com.composechef.taskmate.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Indigo = Color(0xFF5654D6)
val Ink = Color(0xFF252638)
val Canvas = Color(0xFFFAF9F6)
val Muted = Color(0xFF737480)

val Night = Color(0xFF19191F)
val Mist = Color(0xFFE6E5EC)

/**
 * Les couleurs d'une liste : [container] pour le fond de sa carte, [onContainer] pour le texte posé
 * dessus, et [accent] pour les détails (initiales, nombre de membres, icônes).
 */
@Immutable
data class ListColor(val container: Color, val onContainer: Color, val accent: Color)

/** Palette des listes en thème clair. L'index correspond à `SharedList.color`. */
val LightListColors =
    listOf(
        ListColor(container = Color(0xFFEEEDFF), onContainer = Ink, accent = Indigo),
        ListColor(container = Color(0xFFEAF2E9), onContainer = Ink, accent = Color(0xFF4E7551)),
        ListColor(container = Color(0xFFFFF0DD), onContainer = Ink, accent = Color(0xFF966227)),
        ListColor(container = Color(0xFFF9E9EF), onContainer = Ink, accent = Color(0xFFA44D70)),
    )

/** Même palette en thème sombre : des fonds sombres teintés, et des accents éclaircis. */
val DarkListColors =
    listOf(
        ListColor(container = Color(0xFF2E2D4F), onContainer = Mist, accent = Color(0xFFBEBBFF)),
        ListColor(container = Color(0xFF263328), onContainer = Mist, accent = Color(0xFFA5D0A8)),
        ListColor(container = Color(0xFF3A2F21), onContainer = Mist, accent = Color(0xFFF0BD84)),
        ListColor(container = Color(0xFF3B2530), onContainer = Mist, accent = Color(0xFFF2AAC6)),
    )
