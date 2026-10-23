package com.composechef.taskmate.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Welcome : NavKey

@Serializable
data object SignIn : NavKey

@Serializable
data object SignUp : NavKey

@Serializable
data object ResetPassword : NavKey

@Serializable
data object Lists : NavKey

@Serializable
data object MyTasks : NavKey

@Serializable
data object Profile : NavKey
