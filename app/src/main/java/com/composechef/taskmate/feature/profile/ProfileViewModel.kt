package com.composechef.taskmate.feature.profile

import androidx.lifecycle.ViewModel
import com.composechef.taskmate.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun signOut() {
        authRepository.signOut()
    }
}
