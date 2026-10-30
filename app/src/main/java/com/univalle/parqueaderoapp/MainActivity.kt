package com.univalle.parqueaderoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.univalle.parqueaderoapp.core.network.NetworkConnectivityObserver
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.navigation.AppNavGraph
import com.univalle.parqueaderoapp.ui.shared.screens.SinConexionScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = koinViewModel()
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
