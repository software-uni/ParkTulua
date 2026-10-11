package com.univalle.parqueaderoapp.ui.componet.botones.card

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.Acento
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import com.univalle.parqueaderoapp.R
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonGoogle
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonSecundario
import com.univalle.parqueaderoapp.ui.theme.EnlaceLogin
import com.univalle.parqueaderoapp.ui.theme.SubtituloLogin
import com.univalle.parqueaderoapp.ui.theme.TituloLogin

/**
 * Hoja de bienvenida del login. Medidas tomadas del Figma "Conductor 3.0 / Login":
 * - Alto de la hoja: 528 dp, pegada al borde inferior
 * - Esquinas superiores: 32 dp
 * - Margen lateral: 24 dp, margen superior: 40 dp
 * - Separación encabezado -> botones: 32 dp
 * - Separación entre botones: 14 dp
 *
 * El Figma NO tiene campos de correo ni contraseña en esta hoja, por eso
 * ya no se reciben aquí. Cuando exista la pantalla de "Iniciar sesión"
 * con campos, ahí se usan email/contrasena del ViewModel.
 */
@Composable
fun LoginCard(
    onIniciarSesionClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onCrearCuentaClick: () -> Unit,
    onInvitadoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 528.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Neutro0,
        shadowElevation = 16.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
        ) {
            LoginEncabezado()
            Spacer(Modifier.height(32.dp))
            LoginAcciones(onIniciarSesionClick, onGoogleClick, onCrearCuentaClick)
            // 22 dp + 10 dp de relleno del enlace = los 32 dp que marca el Figma
            Spacer(Modifier.height(22.dp))
            LoginInvitado(onClick = onInvitadoClick)
        }
    }
}

@Composable
private fun LoginEncabezado() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = stringResource(R.string.login_titulo),
            style = TituloLogin,
            color = Neutro900,
        )
        Text(
            text = stringResource(R.string.login_subtitulo),
            style = SubtituloLogin,
            color = Neutro600,
        )
    }
}

@Composable
private fun LoginAcciones(
    onIniciarSesionClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onCrearCuentaClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        BotonPrincipal(onClick = onIniciarSesionClick)
        BotonGoogle(onClick = onGoogleClick)
        BotonSecundario(onClick = onCrearCuentaClick)
    }
}

@Composable
private fun LoginInvitado(onClick: () -> Unit) {
    // Texto centrado, 15 sp semibold, color acento. El texto de strings.xml
    // debe incluir la flecha: "Ingresar como invitado  →"
    Text(
        text = stringResource(R.string.login_invitado),
        style = EnlaceLogin,
        color = Acento,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 40.dp),
    )
}