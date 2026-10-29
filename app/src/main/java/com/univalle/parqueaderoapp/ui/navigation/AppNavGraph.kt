package com.univalle.parqueaderoapp.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.auth.screen.LoginScreen
import com.univalle.parqueaderoapp.ui.auth.screen.RegistroScreen
import com.univalle.parqueaderoapp.ui.auth.screen.VerificacionScreen
import com.univalle.parqueaderoapp.ui.shared.navegacion.BarraNavegacion
import com.univalle.parqueaderoapp.ui.shared.screens.PantallaProvisional


object AppScreens{
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRO_SCREEN = "registro_screen"
    const val VERIFICACION_SCREEN = "verificacion_screen"
    const val MAPA_SCREEN = "mapa_screen"
    const val RESERVAS_SCREEN = "reservas_screen"
    const val PERFIL_SCREEN = "perfil_screen"
}
@Composable
fun AppNavGraph(authViewModel: AuthViewModel){
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val pestanaActiva = PestanaNavegacion.entries.firstOrNull { it.ruta == rutaActual }

    LaunchedEffect(authViewModel.requiereVerificacion) {
        if (authViewModel.requiereVerificacion) {
            authViewModel.limpiarRequiereVerificacion()
            navController.navigate(AppScreens.VERIFICACION_SCREEN) {
                popUpTo(AppScreens.REGISTRO_SCREEN) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(authViewModel.sesionIniciada) {
        if (authViewModel.sesionIniciada) {
            authViewModel.limpiarSesionIniciada()
            navController.navigate(AppScreens.MAPA_SCREEN) {
                popUpTo(AppScreens.LOGIN_SCREEN) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (pestanaActiva != null) {
                BarraNavegacion(
                    pestanaActiva = pestanaActiva,
                    onPestanaClick = { pestana ->
                        navController.navigate(pestana.ruta) {
                            popUpTo(AppScreens.MAPA_SCREEN) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.LOGIN_SCREEN,
            modifier = Modifier.padding(innerPadding)
        ){
            // 1 Ruta de login
            composable(AppScreens.LOGIN_SCREEN) {
                LoginScreen(
                    viewModel = authViewModel,
                    onIniciarSesionClick = {
                        authViewModel.login()
                    },
                    onCrearCuenta = {
                        navController.navigate(AppScreens.REGISTRO_SCREEN)
                    },
                    onIniciarInvitado = {
                        authViewModel.loginInivitado()
                    }
                )
            }
            composable(AppScreens.REGISTRO_SCREEN) {
                RegistroScreen(
                    viewModel = authViewModel,
                    onVolverLoginClick = { navController.popBackStack() }
                )
            }

            composable(AppScreens.VERIFICACION_SCREEN) {
                VerificacionScreen(
                    viewModel = authViewModel,
                    onVolverLoginClick = {
                        navController.popBackStack(AppScreens.LOGIN_SCREEN, inclusive = false)
                    },
                    onContinuarClick = {
                        navController.navigate(AppScreens.MAPA_SCREEN) {
                            popUpTo(AppScreens.LOGIN_SCREEN) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Pestañas con la barra de navegación (pantallas provisionales)
            composable(AppScreens.MAPA_SCREEN) { PantallaProvisional("Mapa") }
            composable(AppScreens.RESERVAS_SCREEN) { PantallaProvisional("Reservas") }
            composable(AppScreens.PERFIL_SCREEN) { PantallaProvisional("Perfil") }
        }
    }
}