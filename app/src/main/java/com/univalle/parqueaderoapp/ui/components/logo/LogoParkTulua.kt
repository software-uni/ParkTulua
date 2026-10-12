package com.univalle.parqueaderoapp.ui.componet.logo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import com.univalle.parqueaderoapp.R
import com.univalle.parqueaderoapp.ui.theme.Inter

/**
 * Logo de Park Tuluá, armado con las medidas del Figma (página "07 Logo").
 *
 * Isotipo (base de 44): esquinas 14, letra P Inter Bold 28, punto de ubicación
 * (anillo) de 9 ubicado a 29 de la izquierda y 7 de arriba.
 * Todo se escala proporcionalmente con el parámetro [tamano].
 *
 * Reglas del Figma: sobre claro, isotipo morado; sobre morado u oscuro, isotipo
 * blanco. No estirar, no girar, no agregar sombras ni degradados.
 */
@Composable
fun LogoIsotipo(
    tamano: Dp,
    modifier: Modifier = Modifier,
    colorFondo: Color = Primario,
    colorLetra: Color = Neutro0,
    pesoLetra: FontWeight = FontWeight.Bold,
    radioRelativo: Float = 0.318f,   // 14 / 44
    letraRelativa: Float = 0.636f,   // 28 / 44
    conPunto: Boolean = true,
) {
    val densidad = LocalDensity.current
    val tamanoLetra = with(densidad) { (tamano * letraRelativa).toSp() }

    Box(
        modifier = modifier
            .size(tamano)
            .background(colorFondo, RoundedCornerShape(tamano * radioRelativo)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "P",
            color = colorLetra,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = pesoLetra,
                fontSize = tamanoLetra,
                lineHeight = tamanoLetra * 1.25f,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both,
                ),
            ),
            // En el Figma la P queda un poco a la izquierda para dejar sitio al punto
            modifier = Modifier.offset(x = -(tamano * 0.05f)),
        )
        if (conPunto) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = tamano * 0.659f, y = tamano * 0.159f)   // 29/44 y 7/44
                    .size(tamano * 0.2045f)                              // 9/44
                    .border(tamano * 0.045f, colorLetra, CircleShape),
            )
        }
    }
}

/** Palabra "ParkTuluá": "Park" oscuro y "Tuluá" morado (Inter SemiBold 24, espaciado -0.48). */
@Composable
fun LogoPalabra(
    modifier: Modifier = Modifier,
    colorPark: Color = Neutro900,
    colorTulua: Color = Primario,
    tamanoFuente: TextUnit = 24.sp,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = colorPark)) { append("Park") }
            withStyle(SpanStyle(color = colorTulua)) { append("Tuluá") }
        },
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = tamanoFuente,
            letterSpacing = tamanoFuente * -0.02f,
        ),
        modifier = modifier,
    )
}

/** Logo principal: isotipo + palabra, separados 12 dp. Úsalo sobre fondo claro. */
@Composable
fun LogoCompleto(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LogoIsotipo(tamano = 44.dp)
        LogoPalabra()
    }
}

/**
 * Marca grande de la pantalla de entrada (mientras el carro hace la animación).
 * Figma: isotipo blanco de 68, nombre Inter Bold 34, lema Inter Medium 15 al 75 %.
 */
@Composable
fun MarcaSplash(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LogoIsotipo(
            tamano = 68.dp,
            colorFondo = Neutro0,
            colorLetra = Primario,
            pesoLetra = FontWeight.ExtraBold,
            radioRelativo = 0.294f,   // 20 / 68
            letraRelativa = 0.53f,    // 36 / 68
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.marca_nombre),
            color = Neutro0,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                letterSpacing = (-0.68).sp,
            ),
        )
        Spacer(Modifier.height(7.dp))
        Text(
            text = stringResource(R.string.marca_lema),
            color = Neutro0.copy(alpha = 0.75f),
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
            ),
        )
    }
}

/**
 * Marca pequeña de la esquina superior izquierda, cuando ya terminó la animación.
 * Figma: isotipo blanco de 34, nombre Inter Bold 18, separación de 10.
 */
@Composable
fun MarcaSuperior(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LogoIsotipo(
            tamano = 34.dp,
            colorFondo = Neutro0,
            colorLetra = Primario,
            pesoLetra = FontWeight.ExtraBold,
            radioRelativo = 0.294f,   // 10 / 34
            letraRelativa = 0.53f,    // 18 / 34
        )
        Text(
            text = stringResource(R.string.marca_nombre),
            color = Neutro0,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = (-0.18).sp,
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun LogoCompletoPreview() {
    LogoCompleto(modifier = Modifier.size(width = 280.dp, height = 90.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFF5848B8)
@Composable
private fun MarcaSplashPreview() {
    MarcaSplash()
}

@Preview(showBackground = true, backgroundColor = 0xFF5848B8)
@Composable
private fun MarcaSuperiorPreview() {
    MarcaSuperior()
}