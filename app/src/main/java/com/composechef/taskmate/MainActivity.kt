package com.composechef.taskmate

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.feature.auth.WelcomeScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskMateTheme {
                // Pour l'instant, l'app n'affiche que l'écran d'accueil.
                // Tu relieras tous les écrans avec Navigation 3 à l'étape 3 du codelab.
                WelcomeScreen(onGetStarted = {}, onSignIn = {})
            }
        }
    }
}
