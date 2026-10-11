package com.univalle.parqueaderoapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import co.parktulua.core.designsystem.* // Importa tus colores personalizados

// Mapeo para el modo oscuro usando tus tonos oscuros
private val DarkColorScheme = darkColorScheme(
    primary = Primario,
    secondary = Acento,
    background = AdminOscuro1,
    surface = AdminOscuro2,
    onPrimary = Neutro0,
    onBackground = Neutro100,
    onSurface = Neutro100,
    error = Lleno
)

// Mapeo para el modo claro usando tus variables
private val LightColorScheme = lightColorScheme(
    primary = Primario,
    secondary = Acento,
    background = FondoApp,
    surface = Neutro0,
    onPrimary = Neutro0,
    onSecondary = Neutro900,
    onBackground = Neutro900,
    onSurface = Neutro900,
    error = Lleno,
    onError = LlenoFondo
)

@Composable
fun ParqueaderoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Se recomienda false para que la app siempre use los colores de tu marca
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
      //  typography = Typography,
        content = content
    )
}