package com.example.mc_a2.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mc_a2.FlightTrackerApplication
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferencesRepository = (application as FlightTrackerApplication).userPreferencesRepository

    val isLoggedIn: Flow<Boolean> = userPreferencesRepository.isLoggedIn
}
