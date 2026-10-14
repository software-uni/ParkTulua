package com.univalle.parqueaderoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.auth.FirebaseAuthRepository
import com.univalle.parqueaderoapp.data.usuario.ConfiguracionSupabase
import com.univalle.parqueaderoapp.data.usuario.SupabaseUsuarioRepository
import com.univalle.parqueaderoapp.ui.navigation.AppNavGraph
import com.univalle.parqueaderoapp.ui.screens.AuthViewModel
import com.univalle.parqueaderoapp.ui.screens.AuthViewModelFactory
import okhttp3.OkHttpClient


class MainActivity : ComponentActivity() {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val authRepository by lazy { FirebaseAuthRepository(firebaseAuth) }
    private val usuarioRepository by lazy {
        SupabaseUsuarioRepository(
            auth = firebaseAuth,
            cliente = OkHttpClient(),
            urlFuncion = ConfiguracionSupabase.URL_REGISTRAR_USUARIO
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository, usuarioRepository)
            )
            AppNavGraph(authViewModel = authViewModel)
        }
    }
}

