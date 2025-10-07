package com.salaheddine.vitaltrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.salaheddine.vitaltrack.auth.*

@Composable
fun AppRoot(vm: AuthViewModel) {
    val state by vm.state.collectAsState()

    when (state) {
        is AuthState.Success -> HomeScreen(
            user = (state as AuthState.Success).user,
            onLogout = { vm.logout() }
        )
        is AuthState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is AuthState.Error -> AuthScreen(
            error = (state as AuthState.Error).message,
            onLogin = { e, p -> vm.login(e, p) },
            onRegister = { e, p -> vm.register(e, p) }
        )
        AuthState.Idle -> AuthScreen(
            error = null,
            onLogin = { e, p -> vm.login(e, p) },
            onRegister = { e, p -> vm.register(e, p) }
        )
    }
}

@Composable
fun AuthScreen(
    error: String?,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("VitalTrack — Connexion", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = pwd, onValueChange = { pwd = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { onLogin(email.trim(), pwd) }) { Text("Se connecter") }
            OutlinedButton(onClick = { onRegister(email.trim(), pwd) }) { Text("Créer un compte") }
        }
        if (!error.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun HomeScreen(user: AppUser, onLogout: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenue ${user.email}", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onLogout) { Text("Se déconnecter") }
    }
}
