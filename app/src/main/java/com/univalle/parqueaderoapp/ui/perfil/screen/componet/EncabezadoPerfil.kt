package com.univalle.parqueaderoapp.ui.perfil.screen.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.LlenoTexto
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage

@Composable
fun EncabezadoPerfil(
    nombreCompleto: String,
    correo: String,
    fotoUrl: String? = null,
    onEditarClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        // --- 1. AVATAR CON FOTO O INICIALES ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(PrimarioClaro),
                contentAlignment = Alignment.Center
            ) {
                if (!fotoUrl.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = fotoUrl,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        error = { InicialesAvatar(nombreCompleto) }
                    )
                } else {
                    InicialesAvatar(nombreCompleto)

                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // --- 2. DATOS DEL USUARIO ---
                Text(
                    text = nombreCompleto.ifBlank { "Usuario Invitado" },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutro900,

                )
                Text(
                    text = correo.ifBlank { "Sin correo registrado" },
                    fontSize = 13.sp,
                    color = Neutro600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            // --- 3. BOTÓN EDITAR ---
            TextButton(onClick = onEditarClick) {
                Text(
                    text = "Editar",
                    color = Primario,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun obtenerIniciales(nombre: String): String {
    if (nombre.isBlank()) return "?"
    val palabras = nombre.trim().split(" ").filter { it.isNotBlank() }
    return when (palabras.size) {
        0 -> "?"
        1 -> palabras[0].take(2).uppercase()
        else -> "${palabras[0].first()}${palabras[1].first()}".uppercase()
    }
}

@Composable
private fun InicialesAvatar(nombre: String) {
    Text(
        text = obtenerIniciales(nombre),
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Primario
    )
}