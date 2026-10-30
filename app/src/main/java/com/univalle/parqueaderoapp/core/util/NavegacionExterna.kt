package com.univalle.parqueaderoapp.core.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.univalle.parqueaderoapp.domain.model.Coordenada

fun abrirNavegacion(context: Context, destino: Coordenada, nombre: String) {
    val geo = Uri.parse("geo:${destino.latitud},${destino.longitud}?q=${destino.latitud},${destino.longitud}(${Uri.encode(nombre)})")
    val intent = Intent(Intent.ACTION_VIEW, geo)
    try {
        context.startActivity(Intent.createChooser(intent, "Navegar con"))
    } catch (e: ActivityNotFoundException) {
        val web = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${destino.latitud},${destino.longitud}")
        context.startActivity(Intent(Intent.ACTION_VIEW, web))
    }
}