package com.univalle.parqueaderoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.univalle.parqueaderoapp.ui.screens.AuthViewModel
import com.univalle.parqueaderoapp.ui.screens.RegistroScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.vehiculos.SupabaseVehiculoRepository
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilAccion
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilViewModel
import com.univalle.parqueaderoapp.ui.screens.LoginScreen
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilViewModelFactory
import okhttp3.OkHttpClient

object AppScreens {
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRO_SCREEN = "registro_screen"
    const val PERFIL_SCREEN = "perfil_screen"
    const val EDITAR_PERFIL_SCREEN = "editar_perfil_screen"
}

@Composable
fun AppNavGraph(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.LOGIN_SCREEN
    ) {
        // 1. Ruta de Login
        composable(AppScreens.LOGIN_SCREEN) {
            LoginScreen(
                viewModel = authViewModel,
                onIniciarSesionClick = {
                    // Ejecutamos el login y navegamos directamente al perfil
                    navController.navigate(AppScreens.PERFIL_SCREEN) {
                        popUpTo(AppScreens.LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onCrearCuenta = {
                    navController.navigate(AppScreens.REGISTRO_SCREEN)
                },
                onIniciarInvitado = {
                    navController.navigate(AppScreens.PERFIL_SCREEN) {
                        popUpTo(AppScreens.LOGIN_SCREEN) { inclusive = true }
                    }
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

        // 3. Ruta de Perfil (¡Esta era la que faltaba y causaba el cierre!)
        composable(AppScreens.PERFIL_SCREEN) {
            val firebaseAuth = FirebaseAuth.getInstance()
            val vehiculoRepository = SupabaseVehiculoRepository(
                auth = firebaseAuth,
                cliente = OkHttpClient()
            )

            val perfilViewModel: PerfilViewModel = viewModel(
                factory = PerfilViewModelFactory(
                    auth = firebaseAuth,
                    vehiculoRepository = vehiculoRepository
                )
            )

            val estado by perfilViewModel.uiState.collectAsState()

            PerfilScreen(
                estado = estado,
                onAccion = { accion ->
                    perfilViewModel.onAccion(accion)
                    if (accion is PerfilAccion.CerrarSesionClick) {
                        firebaseAuth.signOut()
                        navController.navigate(AppScreens.LOGIN_SCREEN) {
                            popUpTo(AppScreens.PERFIL_SCREEN) { inclusive = true }
                        }
                    }
                }
            )
        }
        // Ruta de Editar Perfil
        composable(AppScreens.EDITAR_PERFIL_SCREEN) {
            val firebaseAuth = FirebaseAuth.getInstance()
            val perfilViewModel: PerfilViewModel = viewModel(
                factory = PerfilViewModelFactory(
                    auth = firebaseAuth,
                    vehiculoRepository = SupabaseVehiculoRepository(
                        auth = firebaseAuth,
                        cliente = OkHttpClient()
                    )
                )
            )
            val estado by perfilViewModel.uiState.collectAsState()

//            EditarPerfilScreen(
//                nombreActual = estado.nombre,
//                correoActual = estado.correo,
//                onGuardarClick = { nuevoNombre ->
//                    perfilViewModel.onAccion(PerfilAccion.GuardarCambiosPerfil(nuevoNombre))
//                    navController.popBackStack() // Regresamos al perfil al guardar
//                },
//                onVolverClick = {
//                    navController.popBackStack()
//                }
//            )
        }
    }
}