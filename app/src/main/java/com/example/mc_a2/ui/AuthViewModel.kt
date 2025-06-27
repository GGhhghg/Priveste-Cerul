package com.example.mc_a2.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mc_a2.data.FlightRepository
import com.example.mc_a2.data.Result
import com.example.mc_a2.data.db.FlightDatabase
import com.example.mc_a2.data.UserPreferencesRepository
import com.example.mc_a2.FlightTrackerApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val message: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FlightDatabase.getDatabase(application)
    private val repository = FlightRepository(database)
    private val userPreferencesRepository: UserPreferencesRepository = (application as FlightTrackerApplication).userPreferencesRepository

    private val _loginUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val loginUiState: StateFlow<AuthUiState> = _loginUiState

    private val _registerUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val registerUiState: StateFlow<AuthUiState> = _registerUiState

    fun login(username: String, password: String) {
        _loginUiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = repository.loginUser(username, password)) {
                is Result.Success -> {
                    if (result.data) {
                        userPreferencesRepository.saveLoginState(true)
                        _loginUiState.value = AuthUiState.Success("Autentificare reușită!")
                    } else {
                        _loginUiState.value = AuthUiState.Error("Autentificare eșuată. Verificați credențialele.")
                    }
                }
                is Result.Error -> _loginUiState.value = AuthUiState.Error(result.message)
                else -> _loginUiState.value = AuthUiState.Error("Eroare necunoscută la autentificare.")
            }
        }
    }

    fun register(username: String, password: String) {
        _registerUiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = repository.registerUser(username, password)) {
                is Result.Success -> {
                    if (result.data) {
                        _registerUiState.value = AuthUiState.Success("Înregistrare reușită! Vă puteți autentifica.")
                    } else {
                        _registerUiState.value = AuthUiState.Error("Înregistrare eșuată. Numele de utilizator ar putea exista deja.")
                    }
                }
                is Result.Error -> _registerUiState.value = AuthUiState.Error(result.message)
                else -> _registerUiState.value = AuthUiState.Error("Eroare necunoscută la înregistrare.")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.saveLoginState(false)
        }
    }

    fun resetLoginUiState() {
        _loginUiState.value = AuthUiState.Idle
    }

    fun resetRegisterUiState() {
        _registerUiState.value = AuthUiState.Idle
    }
}