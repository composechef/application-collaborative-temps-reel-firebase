package com.composechef.taskmate.core.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import kotlinx.coroutines.launch

fun ViewModel.launchWrite(block: suspend () -> Unit) {
    viewModelScope.launch {
        try {
            block()
        } catch (e: FirebaseException) {
            Log.w("TaskMate", "L'écriture a échoué", e)
        }
    }
}
