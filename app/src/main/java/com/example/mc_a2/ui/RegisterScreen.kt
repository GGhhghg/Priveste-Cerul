// app/src/main/java/com/example/mc_a2/ui/RegisterScreen.kt
package com.example.mc_a2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mc_a2.R // Importă R

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit, // Callback la succes
    onNavigateToLogin: () -> Unit, // Callback pentru a naviga la login
    authViewModel: AuthViewModel = viewModel() // Injectează ViewModel-ul de autentificare
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordsMatchError by remember { mutableStateOf(false) } // Stare pentru eroare de nepotrivire a parolelor

    val focusManager = LocalFocusManager.current
    val registerUiState by authViewModel.registerUiState.collectAsState() // Observă starea UI de la ViewModel

    LaunchedEffect(registerUiState) {
        if (registerUiState is AuthUiState.Success) {
            onRegisterSuccess() // Dacă înregistrarea a reușit, apelăm callback-ul de succes (ex: navighează la login)
            authViewModel.resetRegisterUiState() // Resetăm starea ViewModel-ului
        } else if (registerUiState is AuthUiState.Error) {
            // Poți afișa un Snackbar sau un Toast aici pentru a notifica utilizatorul despre eroare
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imaginea de fundal
        Image(
            painter = painterResource(id = R.drawable.plane_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Strat semi-transparent mai opac
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Înregistrare",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nume utilizator") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordsMatchError = false // Resetează eroarea când parola se schimbă
                },
                label = { Text("Parola") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordsMatchError = false // Resetează eroarea când confirmarea se schimbă
                },
                label = { Text("Confirmă Parola") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    if (password == confirmPassword) {
                        authViewModel.register(username, password)
                    } else {
                        passwordsMatchError = true
                    }
                }),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                isError = passwordsMatchError, // Setează starea de eroare vizuală
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )

            if (passwordsMatchError) {
                Text(
                    text = "Parolele nu se potrivesc!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        authViewModel.register(username, password)
                    } else {
                        passwordsMatchError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerUiState !is AuthUiState.Loading && !passwordsMatchError, // Dezactivează dacă se încarcă sau parolele nu se potrivesc
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (registerUiState is AuthUiState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Înregistrare")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Ai deja cont? Autentifică-te", color = Color.White)
            }

            // Afișează mesaje de eroare
            if (registerUiState is AuthUiState.Error) {
                Text(
                    text = (registerUiState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            // Afișează mesaj de succes
            if (registerUiState is AuthUiState.Success) {
                Text(
                    text = (registerUiState as AuthUiState.Success).message,
                    color = Color.Green, // Poți alege o culoare verde pentru succes
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}