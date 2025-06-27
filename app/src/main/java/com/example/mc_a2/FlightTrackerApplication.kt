package com.example.mc_a2

import android.app.Application
import androidx.work.Configuration
import com.example.mc_a2.data.UserPreferencesRepository
import com.example.mc_a2.workers.FlightDataManager

class FlightTrackerApplication : Application(), Configuration.Provider {

    lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var flightDataManager: FlightDataManager

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(this)
        flightDataManager = FlightDataManager(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
