package com.univalle.parqueaderoapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.repository.PlayServicesUbicacionRepository
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.auth.screen.LoginScreen
import com.univalle.parqueaderoapp.ui.auth.screen.RegistroScreen
import com.univalle.parqueaderoapp.ui.auth.screen.VerificacionScreen
import com.univalle.parqueaderoapp.ui.mapa.MapaScreen
import com.univalle.parqueaderoapp.ui.mapa.MapaViewModel
import com.univalle.parqueaderoapp.ui.mapa.MapaViewModelFactory
import com.univalle.parqueaderoapp.ui.perfil.screen.EditarPerfilScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilAccion
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilScreen
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilViewModel
import com.univalle.parqueaderoapp.ui.shared.navegacion.BarraNavegacion
import com.univalle.parqueaderoapp.ui.shared.screens.PantallaProvisional
import com.univalle.parqueaderoapp.ui.vehiculo.screen.AgregarVehiculoScreen
import org.koin.androidx.compose.koinViewModel

object AppScreens {
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRO_SCREEN = "registro_screen"
    const val VERIFICACION_SCREEN = "verificacion_screen"
    const val MAPA_SCREEN = "mapa_screen"
    const val RESERVAS_SCREEN = "reservas_screen"
    const val PERFIL_SCREEN = "perfil_screen"
    const val EDITAR_PERFIL_SCREEN = "editar_perfil_screen"
    const val AGREGAR_VEHICULO_SCREEN = "agregar_vehiculo_screen"
}


@Composable
fun AppNavGraph(authViewModel: AuthViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val contexto = androidx.compose.ui.platform.LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val pestanaActiva = PestanaNavegacion.entries.firstOrNull { it.ruta == rutaActual }

    // Efectos para la navegación automática de autenticación
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
        ) {
            // 1. Ruta de Login
            composable(AppScreens.LOGIN_SCREEN) {
                LoginScreen(
                    viewModel = authViewModel,
                    onIniciarSesionClick = { authViewModel.login() },
                    onCrearCuenta = { navController.navigate(AppScreens.REGISTRO_SCREEN) },
                    onIniciarInvitado = { authViewModel.loginInivitado() }
                )
            }

            // 2. Ruta de Registro
            composable(AppScreens.REGISTRO_SCREEN) {
                RegistroScreen(
                    viewModel = authViewModel,
                    onVolverLoginClick = { navController.popBackStack() }
                )
            }

            // 3. Ruta de Verificación
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

            // 4. Pestañas principales y navegación inferior
            composable(
                route = AppScreens.MAPA_SCREEN,
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                val context = LocalContext.current.applicationContext
                val mapaViewModel: MapaViewModel = viewModel(
                    factory = MapaViewModelFactory(PlayServicesUbicacionRepository(context))
                )
                MapaScreen(viewModel = mapaViewModel)
            }

            composable(AppScreens.RESERVAS_SCREEN) { PantallaProvisional("Reservas") }

            // 5. Ruta de Perfil (Con Koin y transiciones animadas)
            composable(
                route = AppScreens.PERFIL_SCREEN,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) { backStackEntry ->
                val perfilViewModel: PerfilViewModel =
                    koinViewModel(viewModelStoreOwner = backStackEntry)
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

                            is PerfilAccion.AgregarVehiculoClick -> {
                                navController.navigate(AppScreens.AGREGAR_VEHICULO_SCREEN)
                            }

                            else -> {}
                        }
                    }
                )
            }

            // 6. Ruta de Editar Perfil (Comparte el ViewModel con el padre usando Koin)
            composable(AppScreens.EDITAR_PERFIL_SCREEN) { backStackEntryEditar ->
                val parentEntry = androidx.compose.runtime.remember(backStackEntryEditar) {
                    navController.getBackStackEntry(AppScreens.PERFIL_SCREEN)
                }

                val perfilViewModel: PerfilViewModel =
                    koinViewModel(viewModelStoreOwner = parentEntry)
                val estado by perfilViewModel.uiState.collectAsState()

                EditarPerfilScreen(
                    nombreActual = estado.nombre,
                    correoActual = estado.correo,
                    fotoUrlActual = estado.fotoUrl,
                    onGuardarClick = { nuevoNombre, nuevaFotoUri ->
                        val fotoBytes = nuevaFotoUri?.let { uri ->
                            contexto.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                        }
                        perfilViewModel.onAccion(
                            PerfilAccion.GuardarCambiosPerfil(
                                nuevoNombre,
                                fotoBytes
                            )
                        )
                        navController.popBackStack()
                    },
                    mensajeAccion = estado.mensajeAccion,
                    onMensajeMostrado = {
                        perfilViewModel.onAccion(PerfilAccion.MensajeMostrado)
                    },
                    onEliminarCuentaConfirmado = {
                        perfilViewModel.onAccion(PerfilAccion.EliminarCuentaConfirmado)
                    },
                    onVolverClick = { navController.popBackStack() }
                )
            }

            // 7. Ruta de Agregar Vehículo
            composable(
                route = AppScreens.AGREGAR_VEHICULO_SCREEN,
                enterTransition = { slideInVertically(initialOffsetY = { it }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it }) },
                popEnterTransition = { slideInVertically(initialOffsetY = { -it }) },
                popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
            ) {
                AgregarVehiculoScreen(
                    onRegistroExitoso = { navController.popBackStack() },
                    onVolverClick = { navController.popBackStack() }
                )
            }
        }
    }
}