package com.univalle.parqueaderoapp.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.auth.screen.LoginScreen
import com.univalle.parqueaderoapp.ui.auth.screen.RegistroScreen
import com.univalle.parqueaderoapp.ui.auth.screen.VerificacionScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.EditarPerfilScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilAccion
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilViewModel
import org.koin.androidx.compose.koinViewModel

object AppScreens {
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRO_SCREEN = "registro_screen"
    const val VERIFICACION_SCREEN = "verificacion_screen"
    const val PERFIL_SCREEN = "perfil_screen"
    const val EDITAR_PERFIL_SCREEN = "editar_perfil_screen"
}

@Composable
fun AppNavGraph(authViewModel: AuthViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val contexto = androidx.compose.ui.platform.LocalContext.current
    val firebaseAuth = remember { FirebaseAuth.getInstance() }

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
                    navController.navigate(AppScreens.PERFIL_SCREEN) {
                        popUpTo(AppScreens.VERIFICACION_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            AppScreens.PERFIL_SCREEN,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it })
            }
        ) { backStackEntry ->
            val perfilViewModel: PerfilViewModel = koinViewModel(viewModelStoreOwner = backStackEntry)
            val estado by perfilViewModel.uiState.collectAsState()

            PerfilScreen(
                estado = estado,
                onAccion = { accion ->
                    perfilViewModel.onAccion(accion)
                    when (accion) {
                        is PerfilAccion.CerrarSesionClick -> {
                            firebaseAuth.signOut()
                            navController.navigate(AppScreens.LOGIN_SCREEN) {
                                popUpTo(AppScreens.PERFIL_SCREEN) { inclusive = true }
                            }
                        }
                        is PerfilAccion.EditarPerfilClick -> {
                            navController.navigate(AppScreens.EDITAR_PERFIL_SCREEN)
                        }
                        else -> {}
                    }
                }
            )
        }

        composable(AppScreens.EDITAR_PERFIL_SCREEN) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppScreens.PERFIL_SCREEN)
            }
            val perfilViewModel: PerfilViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
            val estado by perfilViewModel.uiState.collectAsState()

            EditarPerfilScreen(
                nombreActual = estado.nombre,
                correoActual = estado.correo,
                fotoUrlActual = estado.fotoUrl,
                onGuardarClick = { nuevoNombre, nuevaFotoUri ->
                    val fotoBytes = nuevaFotoUri?.let { uri ->
                        contexto.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    }
                    perfilViewModel.onAccion(PerfilAccion.GuardarCambiosPerfil(nuevoNombre, fotoBytes))
                    navController.popBackStack()
                },
                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
