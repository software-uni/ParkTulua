package com.univalle.parqueaderoapp.ui.perfil.screen.componet

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Acento
import co.parktulua.core.designsystem.Neutro900
import coil.compose.SubcomposeAsyncImage

@Composable
fun SelectorFotoPerfil(
    fotoUrlActual: String?,
    nuevaFotoUri: Uri?,
    onFotoSeleccionada: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectorDeFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onFotoSeleccionada(uri)
    }
    val abrirGaleria = {
        selectorDeFoto.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    // Si hay foto nueva se muestra esa; si no, la actual de Supabase
    val modeloImagen: Any? = nuevaFotoUri ?: fotoUrlActual

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(112.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(onClick = abrirGaleria),
                contentAlignment = Alignment.Center
            ) {
                if (modeloImagen != null) {
                    SubcomposeAsyncImage(
                        model = modeloImagen,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = { IconoAgregarFoto() }
                    )
                } else {
                    IconoAgregarFoto()
                }
            }

            // Lápiz sobre la esquina de la foto
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Acento)
                    .clickable(onClick = abrirGaleria),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Cambiar foto",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (modeloImagen != null) "Cambiar foto" else "Agregar foto",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Neutro900
        )
    }
}

@Composable
private fun IconoAgregarFoto() {
    Icon(
        imageVector = Icons.Default.AddAPhoto,
        contentDescription = "Agregar foto",
        tint = Acento,
        modifier = Modifier.size(36.dp)
    )
}