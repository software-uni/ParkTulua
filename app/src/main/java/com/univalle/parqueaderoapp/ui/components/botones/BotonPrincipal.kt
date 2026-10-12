
package com.univalle.parqueaderoapp.ui.componet.botones.botones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Primario
import com.univalle.parqueaderoapp.ui.theme.ButtonText

@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val forma = RoundedCornerShape(16.dp)
    Surface(
        onClick = onClick,
        // Sombra morada del Figma (0, 8, blur 20, 35 % de opacidad)
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = forma,
                ambientColor = Primario.copy(alpha = 0.35f),
                spotColor = Primario.copy(alpha = 0.35f),
            ),
        shape = forma,
        color = Primario,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = texto,
                style = ButtonText,
                color = Neutro0,
            )
        }
    }
}

