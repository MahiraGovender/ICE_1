package com.example.betwise.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val database = BetWiseDatabase.getDatabase(application)
    private val userDao = database.userDao()

    // State flows for UI state
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    // Login function
    fun loginUser(userName: String, password: String) {
        viewModelScope.launch {
            try {
                _loginUiState.value = _loginUiState.value.copy(isLoading = true, error = null)

                val user = userDao.loginUser(userName, password)
                if (user != null) {
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        user = user
                    )
                } else {
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = "Invalid username or password"
                    )
                }
            } catch (e: Exception) {
                _loginUiState.value = _loginUiState.value.copy(
                    isLoading = false,
                    error = "Login failed: ${e.message}"
                )
            }
        }
    }

    // Register function
    fun registerUser(fullName: String, userName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _registerUiState.value = _registerUiState.value.copy(isLoading = true, error = null)

                // Check if user already exists
                val existingUser = userDao.getByUserName(userName)
                if (existingUser != null) {
                    _registerUiState.value = _registerUiState.value.copy(
                        isLoading = false,
                        error = "Username already exists"
                    )
                    return@launch
                }

                // Create and insert new user
                val newUser = userEntity(
                    userName = userName,
                    email = email,
                    password = password,
                    fullName = fullName
                )

                userDao.insertUser(newUser)
                _registerUiState.value = _registerUiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _registerUiState.value = _registerUiState.value.copy(
                    isLoading = false,
                    error = "Registration failed: ${e.message}"
                )
            }
        }
    }

    // Reset states
    fun resetLoginState() {
        _loginUiState.value = LoginUiState()
    }

    fun resetRegisterState() {
        _registerUiState.value = RegisterUiState()
    }
}

// Data classes for UI state
data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: userEntity? = null
)

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)