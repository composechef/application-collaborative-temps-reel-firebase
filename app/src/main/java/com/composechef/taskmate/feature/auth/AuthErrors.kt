package com.composechef.taskmate.feature.auth

import androidx.annotation.StringRes
import com.composechef.taskmate.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

@StringRes
internal fun FirebaseException.toErrorMessage(): Int =
    when (this) {
        is FirebaseAuthWeakPasswordException -> R.string.error_weak_password
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> R.string.error_invalid_credentials
        is FirebaseAuthUserCollisionException -> R.string.error_email_in_use
        is FirebaseNetworkException -> R.string.error_network
        else -> R.string.error_unknown
    }
