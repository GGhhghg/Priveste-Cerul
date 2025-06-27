package com.example.mc_a2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mc_a2.data.model.Flight
import com.example.mc_a2.data.model.LiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.Image // Adaugă acest import
import androidx.compose.ui.layout.ContentScale // Adaugă acest import
import androidx.compose.ui.res.painterResource // Adaugă acest import
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.draw.clip
import android.util.Log // Import for logging
import com.example.mc_a2.R // Asigură-te că acest import există


@Composable
fun FlightTrackerScreen(
    uiState: FlightTrackingState,
    lastFetchTime: String?,
    isTrackingStopped: Boolean,
    onTrackFlight: (String) -> Unit,
    onStopTracking: () -> Unit,
    onNavigateToStats: () -> Unit,
    onResumeTracking: () -> Unit = {},
    onLogout: () -> Unit
) {
    var flightNumber by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Automatically track a default flight or last tracked flight on initial load
    LaunchedEffect(Unit) {
        // You might want to load a default flight number here, e.g., from preferences
        // For now, let's assume it's empty initially and the user will input
        // If you have a mechanism to persist the last tracked flight, load it here
        // if (flightNumber.isNotBlank()) {
        //     onTrackFlight(flightNumber)
        // }
    }

    // Log the current UI state whenever it changes
    LaunchedEffect(uiState) {
        Log.d("FlightTrackerScreen", "UI State: $uiState")
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Imaginea de fundal
        Image(
            painter = painterResource(id = R.drawable.plane_background), // Asigură-te că imaginea ta este numită 'plane_background'
            contentDescription = null, // Nu este necesară o descriere pentru imaginea de fundal
            contentScale = ContentScale.Crop, // Va face ca imaginea să umple întregul spațiu
            modifier = Modifier.fillMaxSize()
        )

        // 2. Un strat semi-transparent pentru lizibilitate (opțional, dar recomandat)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // Poți ajusta culoarea și transparența (alpha)
        )

        // 3. Conținutul existent al ecranului (acum plasat peste imaginea de fundal și stratul semi-transparent)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp) // Păstrăm padding-ul original al Column-ului
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Priveste cerul",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Poate fi necesar să schimbi culoarea textului pentru lizibilitate
                )

                Button(onClick = onNavigateToStats,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Blue,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )) {
                    Text("Informatii")
                }

                Button(onClick = onLogout,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Red,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )) {
                    Text("Logout")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input field for flight number
            OutlinedTextField(
                value = flightNumber,
                onValueChange = { flightNumber = it.uppercase() },
                label = { Text("Numarul zborului") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onTrackFlight(flightNumber)
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Track/Stop buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onTrackFlight(flightNumber)
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Brush.linearGradient(
                                colors = listOf(Color(0xFF03A9F4), Color(0xFF00BCD4)),
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                end = Offset(Float.POSITIVE_INFINITY, 0f)
                            ))
                            .clip(RoundedCornerShape(8.dp))
                            .padding(vertical = 12.dp), // Adjust vertical padding as needed
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Urmareste zborul")
                    }
                }

                Button(
                    onClick = {
                        if (isTrackingStopped) {
                            onResumeTracking()
                        } else {
                            onStopTracking()
                        }
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Brush.linearGradient(
                                colors = listOf(Color(0xFF03A9F4), Color(0xFF00BCD4)),
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                end = Offset(Float.POSITIVE_INFINITY, 0f)
                            ))
                            .clip(RoundedCornerShape(8.dp))
                            .padding(vertical = 12.dp), // Adjust vertical padding as needed
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (isTrackingStopped) "Reia urmarirea" else "Opreste urmarirea")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show last fetch time or tracking stopped message
            if (isTrackingStopped) {
                Text(
                    text = "Tracking stopped.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error // Poate fi necesar să schimbi culoarea pentru lizibilitate pe fundalul nou
                )
            } else if (lastFetchTime != null) {
                Text(
                    text = "Ultima actualizare: $lastFetchTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White // Poate fi necesar să schimbi culoarea textului pentru lizibilitate
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is FlightTrackingState.Initial -> {
                        Text(
                            text = "Introdu un numar de zbor pentru a incepe urmarirea",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White // Poate fi necesar să schimbi culoarea textului pentru lizibilitate
                        )
                    }

                    is FlightTrackingState.Loading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Se incarca...",
                                color = Color.White // Poate fi necesar să schimbi culoarea textului pentru lizibilitate
                            )
                        }
                    }

                    is FlightTrackingState.Success -> {
                        // Conținutul detaliilor zborului ar trebui să fie deasupra fundalului.
                        // Verifică dacă culorile cardurilor și textului din FlightInfoContent sunt lizibile.
                        FlightInfoContent(
                            flight = uiState.flight,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    is FlightTrackingState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.error // Poate fi necesar să schimbi culoarea pentru lizibilitate
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White // Poate fi necesar să schimbi culoarea textului pentru lizibilitate
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlightInfoContent(
    flight: Flight,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        // Flight header information
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f) // Ajustează opacitatea cardului
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    flight.airline?.name?.let { airlineName ->
                        Text(
                            text = airlineName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    flight.flightInfo?.iata?.let { flightIata ->
                        Text(
                            text = "Zborul $flightIata",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                flight.flightStatus?.let { status ->
                    Text(
                        text = "Status: $status",
                        style = MaterialTheme.typography.bodyLarge,
                        color = when (status.lowercase()) {
                            "activ" -> Color.Green
                            "in timp" -> Color.Blue
                            "intarziat" -> Color.Red
                            "aterizat" -> Color.Green
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Route information
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f) // Ajustează opacitatea cardului
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Informatii ruta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Departure information
                    Column {
                        Text(
                            text = "De la",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = flight.departure?.iata ?: "N/A",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = flight.departure?.airport ?: "N/A",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Flight direction indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Divider(
                            modifier = Modifier
                                .width(100.dp)
                                .padding(vertical = 16.dp)
                        )
                    }

                    // Arrival information
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Catre",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = flight.arrival?.iata ?: "N/A",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = flight.arrival?.airport ?: "N/A",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Time information
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f) // Ajustează opacitatea cardului
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Program",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Departure time
                    Column {
                        Text(
                            text = "Decolare",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = formatDateTime(flight.departure?.scheduled) ?: "N/A",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        if (flight.departure?.delay != null && flight.departure.delay > 0) {
                            Text(
                                text = "Intarziat cu ${flight.departure.delay} min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    // Arrival time
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Aterizare",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = formatDateTime(flight.arrival?.scheduled) ?: "N/A",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        if (flight.arrival?.delay != null && flight.arrival.delay > 0) {
                            Text(
                                text = "Delayed by ${flight.arrival.delay} min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Flight time information
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f) // Ajustează opacitatea cardului
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Durata zborului",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            val flightTime = calculateFlightTimeDisplay(flight)
                            Text(
                                text = flightTime,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live data if available
        flight.live?.let { liveData ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f) // Ajustează opacitatea cardului
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Date in timp real",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Updated time
                    liveData.updated?.let { updated ->
                        Text(
                            text = "Ultia actualizare: ${formatDateTime(updated)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Flight parameters
                    liveData.altitude?.let { altitude ->
                        FlightParameter("Altitudine", "$altitude ft")
                    }

                    liveData.speedHorizontal?.let { speed ->
                        FlightParameter("Viteza", "$speed km/h")
                    }

                    liveData.speedVertical?.let { vspeed ->
                        FlightParameter("Viteza Verticala", "$vspeed m/s")
                    }

                    liveData.direction?.let { direction ->
                        FlightParameter("Directie", "$direction°")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Location information in text format
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Current Position: " +
                                    "${liveData.latitude?.toString()?.take(7) ?: "N/A"}, " +
                                    "${liveData.longitude?.toString()?.take(7) ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Visual indicator for ground status
                    Spacer(modifier = Modifier.height(16.dp))

                    liveData.isGround?.let { isGround ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        color = if (isGround) Color.Red else Color.Green,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = if (isGround) "Pe Pamant" else "In Aer",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        } ?: run {
            // Show message when live data is not available
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f) // Ajustează opacitatea cardului
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Data in timp real indisponibile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Zborul nu se afla in decurs de derulare sau datele in timp real nu sunt disponibile",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun FlightParameter(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

// Helper function to format date-time
fun formatDateTime(isoDateTime: String?): String? {
    if (isoDateTime == null) return null

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(isoDateTime) ?: return isoDateTime
        val outputFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        isoDateTime
    }
}

// Helper function to get current time
fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date())
}

// Helper function to calculate flight time display
fun calculateFlightTimeDisplay(flight: Flight): String {
    val actualDepartureTime = flight.departure?.actual?.let { parseDateTime(it)?.time }
    val actualArrivalTime = flight.arrival?.actual?.let { parseDateTime(it)?.time }

    val scheduledDepartureTime = flight.departure?.scheduled?.let { parseDateTime(it)?.time }
    val scheduledArrivalTime = flight.arrival?.scheduled?.let { parseDateTime(it)?.time }

    val departureDelayMinutes = flight.departure?.delay
    val arrivalDelayMinutes = flight.arrival?.delay

    val flightTimeMinutes = if (actualArrivalTime != null && actualDepartureTime != null) {
        ((actualArrivalTime - actualDepartureTime) / 60_000).toInt()
    } else if (scheduledDepartureTime != null && scheduledArrivalTime != null) {
        val departureDelayMs = departureDelayMinutes?.times(60_000L) ?: 0L
        val arrivalDelayMs = arrivalDelayMinutes?.times(60_000L) ?: 0L

        (((scheduledArrivalTime + arrivalDelayMs) - (scheduledDepartureTime + departureDelayMs)) / 60_000).toInt()
    } else {
        null
    }

    return if (flightTimeMinutes != null && flightTimeMinutes > 0) {
        val hours = flightTimeMinutes / 60
        val minutes = flightTimeMinutes % 60
        "${hours}h ${minutes}m"
    } else {
        "N/A"
    }
}

// Helper function to parse date-time
fun parseDateTime(isoDateTime: String): Date? {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFormat.parse(isoDateTime)
    } catch (e: Exception) {
        null
    }
}