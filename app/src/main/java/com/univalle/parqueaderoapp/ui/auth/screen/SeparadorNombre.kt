package com.univalle.parqueaderoapp.ui.perfil.screen

fun separarNombre(nombreCompleto: String): Pair<String, String> {
    val palabras = nombreCompleto.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    return when (palabras.size) {
        0 -> "" to ""
        1 -> palabras[0] to ""
        2 -> palabras[0] to palabras[1]
        3 -> palabras[0] to palabras.drop(1).joinToString(" ")
        else -> {
            val mitad = palabras.size / 2
            palabras.take(mitad).joinToString(" ") to palabras.drop(mitad).joinToString(" ")
        }
    }
}