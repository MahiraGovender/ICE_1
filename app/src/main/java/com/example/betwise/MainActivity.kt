package com.example.betwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.betwise.data.AuthViewModel
import com.example.betwise.data.LoginUiState
import com.example.betwise.data.RegisterUiState
import com.example.betwise.ui.theme.BetWiseTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BetWiseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Nav(innerPadding)
                }
            }
        }
    }
}

@Composable
fun BetWiseLoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val loginUiState by viewModel.loginUiState.collectAsState()
    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(loginUiState.isSuccess) {
        if (loginUiState.isSuccess) {
            navController.navigate("home") { popUpTo("login") { inclusive = true } }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            isError = loginUiState.error != null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = null)
            },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = loginUiState.error != null,
            modifier = Modifier.fillMaxWidth()
        )

        loginUiState.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FilledTonalButton(
            onClick = {
                if (userName.isNotBlank() && password.isNotBlank()) {
                    viewModel.loginUser(userName, password)
                }
            },
            enabled = !loginUiState.isLoading && userName.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loginUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Don't have an account? Register",
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable {
                navController.navigate("register")
                viewModel.resetLoginState()
            }
        )
    }
}

@Composable
fun BetWiseRegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val registerUiState by viewModel.registerUiState.collectAsState()
    var fullName by rememberSaveable { mutableStateOf("") }
    var userName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(registerUiState.isSuccess) {
        if (registerUiState.isSuccess) {
            navController.navigate("login") {
                // popUpTo("register") { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            isError = registerUiState.error?.contains("Username", ignoreCase = true) == true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordConfirm,
            onValueChange = { passwordConfirm = it },
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        registerUiState.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FilledTonalButton(
            onClick = {
                if (fullName.isNotBlank() &&
                    userName.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    password == passwordConfirm) {
                    viewModel.registerUser(fullName, userName, email, password)
                }
            },
            enabled = !registerUiState.isLoading &&
                    fullName.isNotBlank() &&
                    userName.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    password == passwordConfirm,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (registerUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Already have an account? Login",
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable {
                navController.navigate("login")
                viewModel.resetRegisterState()
            }
        )
    }
}

@Composable
fun Nav(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login", modifier = Modifier.padding(innerPadding)) {
        composable("login") {
            BetWiseLoginScreen(navController = navController)
        }
        composable("register") {
            BetWiseRegisterScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BetWiseLoginScreenPreview() {
    BetWiseTheme {
        Surface {
            val navController = rememberNavController()
            BetWiseLoginScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BetWiseRegisterScreenPreview() {
    BetWiseTheme {
        Surface {
            val navController = rememberNavController()
            BetWiseRegisterScreen(navController = navController)
        }
    }
}