package com.univalle.parqueaderoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.core.network.NetworkConnectivityObserver
import com.univalle.parqueaderoapp.data.auth.FirebaseAuthRepository
import com.univalle.parqueaderoapp.data.usuario.ConfiguracionSupabase
import com.univalle.parqueaderoapp.data.usuario.SupabaseUsuarioRepository
import com.univalle.parqueaderoapp.ui.navigation.AppNavGraph
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModelFactory
import com.univalle.parqueaderoapp.ui.shared.screens.SinConexionScreen
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
        //
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository, usuarioRepository)
            )
            //1. obtenemos el contexto y creamos el observador
            val context = LocalContext.current
            val networkObserver = remember { NetworkConnectivityObserver(context) }
            val isConnected by networkObserver.isConnected.collectAsState(initial = true)
            Box(modifier = Modifier.fillMaxSize()) {
                AppNavGraph(authViewModel = authViewModel)
                if (!isConnected) {
                    SinConexionScreen(onReintentarClick = {})
                }
            }
        }
    }
}

