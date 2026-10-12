package co.parktulua.core.designsystem

import androidx.compose.ui.graphics.Color

// Park Tuluá · colores del diseño (fuente: Figma "Park Tulua").
// En Compose los colores son constantes (val): no cambian mientras la app corre.

// ===== Marca =====
val Primario = Color(0xFF6D5BD0) //Botones principales, selección, pin elegido
val PrimarioOscuro = Color(0xFF5848B8) //Texto sobre fondos morados claros, botones secundarios
val PrimarioClaro = Color(0xFFEEEBFB) //	Fondos de botones secundarios, chips, miniaturas
val Acento = Color(0xFF1F9BB0) //Enlaces, franja del carro, “P” del parqueadero
val AcentoClaro = Color(0xFFE0F3F6) //Fondos de avisos informativos

// ===== Estados =====
val Disponible = Color(0xFF2FA67C) //Punto de cupo disponible
val DisponibleFondo = Color(0xFFE4F5EE) //	Etiqueta “12 cupos”
val DisponibleTexto = Color(0xFF1F7A5C) //Texto de cupos y pagos confirmados
val Poca = Color(0xFFF59E0B) //Ofertas, descuentos, calificación
val PocaFondo = Color(0xFFFEF3C7) //Fondo de ofertas
val PocaTexto = Color(0xFF92400E) //Texto de ofertas
val Lleno = Color(0xFFDC2626) //Errores, cancelar, luz roja
val LlenoFondo = Color(0xFFFEE2E2) //Fondo de errores
val LlenoTexto = Color(0xFF991B1B) //Texto de errores

// ===== Neutros =====
val Neutro900 = Color(0xFF1E1B3A)   // títulos y texto principal Títulos y texto principal
val Neutro600 = Color(0xFF6B7088)   // texto secundario Texto secundario
val Neutro300 = Color(0xFFD9D5EE)  //Líneas y elementos inactivos
val Neutro100 = Color(0xFFF1EFFA) //Fondos suaves
val Neutro0 = Color(0xFFFFFFFF) //Tarjetas y botones
val FondoApp = Color(0xFFF7F6FD) //Fondo de las pantallas
val BordeSuave = Color(0xFFE3E0F3) //	Bordes de tarjetas y campos

// ===== Usados en el diseño y pendientes de oficializar en Figma =====
val IlustracionMoradoOscuro = Color(0xFF2F2475)
val IlustracionEdificio = Color(0xFF4A3CAE)
val IlustracionPuerta = Color(0xFF241C63)
val MapaFondo = Color(0xFFEBE9F6)
val MapaManzana = Color(0xFFDCD8F0)
val Gris400 = Color(0xFF9CA3AF)
val Gris600 = Color(0xFF4B5563)
val Gris700 = Color(0xFF374151)
val AdminOscuro1 = Color(0xFF14122B)
val AdminOscuro2 = Color(0xFF1A1736)
val AdminOscuro3 = Color(0xFF2A2750)
val GrisLila = Color(0xFFA3A1B8)
val GrisLilaClaro = Color(0xFFF0EFF6)
val Piel = Color(0xFFF2C7A5)

// ===== Espaciado y radios (en dp, usar con .dp) =====
object Espaciado {
    const val E4 = 4
    const val E8 = 8
    const val E12 = 12
    const val E16 = 16
    const val E24 = 24
    const val E32 = 32
    const val E48 = 48
}

object Radios {
    const val R8 = 8
    const val R12 = 12
    const val R16 = 16
    const val Pill = 999
}