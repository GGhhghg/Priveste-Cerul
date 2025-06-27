// app/src/main/java/com/example/mc_a2/ui/LoginScreen.kt
package com.example.mc_a2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.* // Importă toate componentele Material3
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
import androidx.lifecycle.viewmodel.compose.viewModel // Importă viewModel
import com.example.mc_a2.R // Importă R pentru a accesa resursele (imaginea de fundal)

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Callback la succes
    onNavigateToRegister: () -> Unit, // Callback pentru a naviga la înregistrare
    authViewModel: AuthViewModel = viewModel() // Injectează ViewModel-ul de autentificare
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val loginUiState by authViewModel.loginUiState.collectAsState() // Observă starea UI de la ViewModel

    // Folosim LaunchedEffect pentru a reacționa la schimbările de stare o singură dată
    LaunchedEffect(loginUiState) {
        if (loginUiState is AuthUiState.Success) {
            onLoginSuccess() // Dacă login-ul a reușit, apelăm callback-ul de succes
            authViewModel.resetLoginUiState() // Resetăm starea ViewModel-ului
        } else if (loginUiState is AuthUiState.Error) {
            // Poți afișa un Snackbar sau un Toast aici pentru a notifica utilizatorul despre eroare
            // Ex: Log.e("LoginScreen", (loginUiState as AuthUiState.Error).message)
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
        // Strat semi-transparent mai opac pentru ecranul de login/register
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Poți ajusta opacitatea
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Autentificare",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White, // Text alb pe fundal întunecat
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nume utilizator") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                // Ajustează culorile pentru câmpurile de text pentru vizibilitate pe fundal
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary, // Culoarea bordurii când e focusat
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f), // Culoarea bordurii când nu e focusat
                    focusedLabelColor = MaterialTheme.colorScheme.primary, // Culoarea etichetei când e focusată
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f), // Culoarea etichetei când nu e focusată
                    cursorColor = MaterialTheme.colorScheme.primary, // Culoarea cursorului
                    focusedTextColor = Color.White, // Culoarea textului introdus când e focusat
                    unfocusedTextColor = Color.White // Culoarea textului introdus când nu e focusat
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Parola") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(), // Ascunde caracterele parolei
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus() // Închide tastatura la Done
                    authViewModel.login(username, password) // Începe procesul de login
                }),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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

            Button(
                onClick = { authViewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginUiState !is AuthUiState.Loading, // Dezactivează butonul când se încarcă
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Folosește culoarea principală a temei tale
                    contentColor = MaterialTheme.colorScheme.onPrimary // Culoarea textului de pe buton
                )
            ) {
                if (loginUiState is AuthUiState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Nu ai cont? Înregistrează-te", color = Color.White) // Text alb pentru link
            }

            // Afișează mesaje de eroare
            if (loginUiState is AuthUiState.Error) {
                Text(
                    text = (loginUiState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error, // Culoare de eroare definită în temă
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}