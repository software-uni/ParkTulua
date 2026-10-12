package com.univalle.parqueaderoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.auth.FirebaseAuthRepository
import com.univalle.parqueaderoapp.ui.navigation.AppNavGraph
import com.univalle.parqueaderoapp.ui.screens.AuthViewModel
import com.univalle.parqueaderoapp.ui.screens.AuthViewModelFactory
import com.univalle.parqueaderoapp.ui.screens.LoginScreen
import com.univalle.parqueaderoapp.ui.theme.ParqueaderoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val firebaseAuth = FirebaseAuth.getInstance()
            val authRepository = FirebaseAuthRepository(firebaseAuth)
            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(
                authRepository
            )
            )
            AppNavGraph(authViewModel = authViewModel)
        }
    }
}

