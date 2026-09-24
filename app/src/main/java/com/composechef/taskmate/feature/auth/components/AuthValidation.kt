package com.composechef.taskmate.feature.auth.components

import android.util.Patterns

/** Longueur minimale d'un mot de passe. Firebase en exige 6, on demande un peu plus. */
internal const val MIN_PASSWORD_LENGTH = 8

internal fun isValidEmail(email: String): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

internal fun isValidPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
