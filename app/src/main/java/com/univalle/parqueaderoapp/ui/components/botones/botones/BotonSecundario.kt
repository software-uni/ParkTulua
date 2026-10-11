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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.PrimarioClaro
import co.parktulua.core.designsystem.PrimarioOscuro
import com.univalle.parqueaderoapp.R
import com.univalle.parqueaderoapp.ui.theme.ButtonText

@Composable
fun BotonSecundario(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = PrimarioClaro,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.boton_crear_cuenta),
                style = ButtonText,
                color = PrimarioOscuro,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BotonSecundarioPreview() {
    BotonSecundario(onClick = {})
}