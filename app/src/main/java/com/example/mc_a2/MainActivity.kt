package com.example.mc_a2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mc_a2.ui.FlightStatisticsScreen
import com.example.mc_a2.ui.FlightStatisticsViewModel
import com.example.mc_a2.ui.FlightTrackerScreen
import com.example.mc_a2.ui.FlightTrackingViewModel
import com.example.mc_a2.ui.theme.MC_A2Theme
import com.example.mc_a2.ui.LoginScreen
import com.example.mc_a2.ui.RegisterScreen
import com.example.mc_a2.ui.AuthViewModel
import com.example.mc_a2.ui.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MC_A2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel(), authViewModel: AuthViewModel = viewModel()) {
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsState(initial = false)
    val startScreen = if (isLoggedIn) Screen.FLIGHT_TRACKER else Screen.LOGIN

    var currentScreen by remember { mutableStateOf(startScreen) }

    when (currentScreen) {
        Screen.LOGIN -> {
            LoginScreen(
                onLoginSuccess = { currentScreen = Screen.FLIGHT_TRACKER },
                onNavigateToRegister = { currentScreen = Screen.REGISTER }
            )
        }
        Screen.REGISTER -> {
            RegisterScreen(
                onRegisterSuccess = { currentScreen = Screen.LOGIN },
                onNavigateToLogin = { currentScreen = Screen.LOGIN }
            )
        }
        Screen.FLIGHT_TRACKER -> {
            FlightTrackerAppWithNav(
                onNavigateToStats = { currentScreen = Screen.STATISTICS },
                onLogout = {
                    authViewModel.logout()
                    currentScreen = Screen.LOGIN
                }
            )
        }
        Screen.STATISTICS -> {
            val viewModel: FlightStatisticsViewModel = viewModel()
            FlightStatisticsScreen(
                viewModel = viewModel,
                onNavigateBack = { currentScreen = Screen.FLIGHT_TRACKER }
            )
        }
    }
}

@Composable
fun FlightTrackerAppWithNav(onNavigateToStats: () -> Unit, onLogout: () -> Unit) {
    val viewModel: FlightTrackingViewModel = viewModel()
    FlightTrackerRoute(
        viewModel = viewModel,
        onNavigateToStats = onNavigateToStats,
        onLogout = onLogout
    )
}

@Composable
fun FlightTrackerRoute(
    viewModel: FlightTrackingViewModel,
    onNavigateToStats: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lastFetchTime by viewModel.lastFetchTime.collectAsState()
    val isTrackingStopped by viewModel.isTrackingStopped.collectAsState()

    FlightTrackerScreen(
        uiState = uiState,
        lastFetchTime = lastFetchTime,
        isTrackingStopped = isTrackingStopped,
        onTrackFlight = { flightNumber ->
            viewModel.trackFlight(flightNumber)
        },
        onStopTracking = {
            viewModel.stopTracking()
        },
        onNavigateToStats = onNavigateToStats,
        onResumeTracking = {
            viewModel.resumeTracking()
        },
        onLogout = onLogout
    )
}

enum class Screen {
    LOGIN,
    REGISTER,
    FLIGHT_TRACKER,
    STATISTICS
}