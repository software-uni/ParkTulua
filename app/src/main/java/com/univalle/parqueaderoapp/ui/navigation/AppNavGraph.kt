package com.univalle.parqueaderoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.vehiculos.SupabaseVehiculoRepository
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.auth.screen.LoginScreen
import com.univalle.parqueaderoapp.ui.auth.screen.RegistroScreen
import com.univalle.parqueaderoapp.ui.auth.screen.VerificacionScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilAccion
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilViewModel
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilViewModelFactory
import okhttp3.OkHttpClient

object AppScreens {
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRO_SCREEN = "registro_screen"
    const val VERIFICACION_SCREEN = "verificacion_screen"
    const val PERFIL_SCREEN = "perfil_screen"
    const val EDITAR_PERFIL_SCREEN = "editar_perfil_screen"
}

@Composable
fun AppNavGraph(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    // Escuchamos si requiere verificacion (Viene de main)
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
    ) {
        // 1. Ruta de Login
        composable(AppScreens.LOGIN_SCREEN) {
            LoginScreen(
                viewModel = authViewModel,
                onIniciarSesionClick = {
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
                onVolverLoginClick = { navController.popBackStack() }
            )
        }

        // 3. Ruta de Verificacion (Viene de main)
        composable(AppScreens.VERIFICACION_SCREEN) {
            VerificacionScreen(
                viewModel = authViewModel,
                onVolverLoginClick = {
                    navController.popBackStack(AppScreens.LOGIN_SCREEN, inclusive = false)
                },
                onContinuarClick = {
                    // Acción para continuar (probablemente ir al perfil)
                    navController.navigate(AppScreens.PERFIL_SCREEN) {
                        popUpTo(AppScreens.VERIFICACION_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        // 4. Ruta de Perfil (Tu código)
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

        // 5. Ruta de Editar Perfil (Tu código)
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

            // NOTA: Lo dejé comentado igual que en tu código.
            // Quítale las barras '//' cuando ya tengas el archivo EditarPerfilScreen.kt importado.

            // EditarPerfilScreen(
            //     nombreActual = estado.nombre,
            //     correoActual = estado.correo,
            //     onGuardarClick = { nuevoNombre ->
            //         perfilViewModel.onAccion(PerfilAccion.GuardarCambiosPerfil(nuevoNombre))
            //         navController.popBackStack()
            //     },
            //     onVolverClick = {
            //         navController.popBackStack()
            //     }
            // )
        }
    }
}