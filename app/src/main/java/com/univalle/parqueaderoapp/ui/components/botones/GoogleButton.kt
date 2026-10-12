package com.univalle.parqueaderoapp.ui.componet.botones.botones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.BordeSuave
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro900
import com.univalle.parqueaderoapp.ui.theme.ButtonText
import com.univalle.parqueaderoapp.R

@Composable
fun BotonGoogle (
    onClick : () -> Unit ,
    modifier : Modifier = Modifier,
){
    Surface(
        onClick = onClick ,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Neutro0,
        border = BorderStroke(1.5.dp, BordeSuave)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally ),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon(
                painter = painterResource(R.drawable.icono_google),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = Color.Unspecified,
            )

            Text(
                text = stringResource(R.string.boton_google),
                style = ButtonText,
                color = Neutro900,
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun BotonGooglePreview() {

        BotonGoogle(onClick = {})

}