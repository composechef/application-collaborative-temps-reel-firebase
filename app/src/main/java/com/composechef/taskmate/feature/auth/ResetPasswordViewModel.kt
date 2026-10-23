package com.composechef.taskmate.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composechef.taskmate.data.AuthRepository
import com.google.firebase.FirebaseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun sendResetEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = ResetPasswordUiState.Sending
            _uiState.value = try {
                authRepository.sendPasswordReset(email)
                ResetPasswordUiState.EmailSent(email)
            } catch (e: FirebaseException) {
                ResetPasswordUiState.Error(e.toErrorMessage())
            }
        }
    }
}
