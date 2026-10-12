
package com.univalle.parqueaderoapp.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.univalle.parqueaderoapp.R

// Park Tuluá · tipografía del diseño (fuente: Figma "Park Tulua").
// Fuente: Inter. Descárgala de Google Fonts y copia estos archivos a app/src/main/res/font/
// con estos nombres: inter_regular.ttf, inter_medium.ttf, inter_semibold.ttf,
// inter_bold.ttf, inter_extrabold.ttf

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extrabold, FontWeight.ExtraBold),
)

// Los 8 estilos definidos en Figma.
val Display = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp)
val H1 = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp)
val H2 = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp)
val H3 = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp)
val Body = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp)
val BodyS = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp)
val Caption = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp)
val ButtonText = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 20.sp)
// Estilos del login, con los tamaños exactos de Figma
val TituloLogin = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 32.sp, letterSpacing = (-0.4).sp)
val SubtituloLogin = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp)
val EnlaceLogin = TextStyle(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp)

// Conexión con el tema de Material 3.
val ParkTuluaTypography = Typography(
    displayMedium = Display,
    headlineLarge = H1,
    headlineMedium = H2,
    titleMedium = H3,
    bodyLarge = Body,
    bodyMedium = BodyS,
    labelSmall = Caption,
    labelLarge = ButtonText,
)