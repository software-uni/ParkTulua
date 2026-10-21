package com.univalle.parqueaderoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.auth.screen.RegistroScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.univalle.parqueaderoapp.ui.auth.screen.LoginScreen
import com.univalle.parqueaderoapp.ui.auth.screen.VerificacionScreen


object AppScreens{
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRO_SCREEN = "registro_screen"
    const val VERIFICACION_SCREEN = "verificacion_screen"
}
@Composable
fun AppNavGraph(authViewModel: AuthViewModel){
    val navController = rememberNavController()

    LaunchedEffect(authViewModel.requiereVerificacion) {
        if (authViewModel.requiereVerificacion) {
            authViewModel.limpiarRequiereVerificacion()
            navController.navigate(AppScreens.VERIFICACION_SCREEN) {
                popUpTo(AppScreens.REGISTRO_SCREEN) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppScreens.LOGIN_SCREEN
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

                }
            )
        }
    }


}