package com.composechef.taskmate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.composechef.taskmate.feature.auth.ResetPasswordRoute
import com.composechef.taskmate.feature.auth.SignInRoute
import com.composechef.taskmate.feature.auth.SignUpRoute
import com.composechef.taskmate.feature.auth.WelcomeScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator


@Composable
fun AuthNavigation() {
    val backStack = rememberNavBackStack(Welcome)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Welcome> {
                WelcomeScreen(
                    onGetStarted = { backStack.add(SignUp) },
                    onSignIn = { backStack.add(SignIn) },
                )
            }
            entry<SignIn> {
                SignInRoute(
                    onForgotPassword = { backStack.add(ResetPassword) },
                    onCreateAccount = { backStack.replaceLast(SignUp) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }

            entry<SignUp> {
                SignUpRoute(
                    onSignIn = { backStack.replaceLast(SignIn) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<ResetPassword> {
                ResetPasswordRoute(onBack = { backStack.removeLastOrNull() })
            }
        },
    )
}