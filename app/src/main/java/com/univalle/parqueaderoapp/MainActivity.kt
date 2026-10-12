package com.univalle.parqueaderoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.auth.FirebaseAuthRepository
import com.univalle.parqueaderoapp.ui.navigation.AppNavGraph
import com.univalle.parqueaderoapp.ui.screens.AuthViewModel
import com.univalle.parqueaderoapp.ui.screens.AuthViewModelFactory


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

