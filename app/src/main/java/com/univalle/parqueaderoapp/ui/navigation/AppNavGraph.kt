package com.univalle.parqueaderoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.univalle.parqueaderoapp.ui.screens.AuthViewModel
import com.univalle.parqueaderoapp.ui.screens.RegistroScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.univalle.parqueaderoapp.ui.screens.LoginScreen


object AppScreens{
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRO_SCREEN = "registro_screen"
}
@Composable
fun AppNavGraph(authViewModel: AuthViewModel){
    val navController = rememberNavController()

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
        // 2. Ruta de Registro
        composable(AppScreens.REGISTRO_SCREEN) {
            RegistroScreen(
                viewModel = authViewModel,
                onVolverLoginClick = {
                    navController.popBackStack()
                }
            )
        }
    }


}