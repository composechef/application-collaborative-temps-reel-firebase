package com.composechef.taskmate.core.model

/** Les initiales d'un nom, par exemple « Yves Kalume » donne « YK ». */
fun initialsOf(name: String): String = name.trim()
    .split(Regex("\\s+"))
    .filter(String::isNotEmpty)
    .take(2)
    .joinToString(separator = "") { it.first().uppercase() }
