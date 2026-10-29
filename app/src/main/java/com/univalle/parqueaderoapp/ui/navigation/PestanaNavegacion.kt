package com.univalle.parqueaderoapp.ui.navigation

enum class PestanaNavegacion(val ruta: String, val etiqueta: String) {
    MAPA(AppScreens.MAPA_SCREEN, "Mapa"),
    RESERVAS(AppScreens.RESERVAS_SCREEN, "Reservas"),
    PERFIL(AppScreens.PERFIL_SCREEN, "Perfil")
}