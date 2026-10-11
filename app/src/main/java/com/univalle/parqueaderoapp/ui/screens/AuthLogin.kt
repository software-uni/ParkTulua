package com.univalle.parqueaderoapp.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import co.parktulua.core.designsystem.IlustracionMoradoOscuro
import co.parktulua.core.designsystem.PrimarioOscuro
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.univalle.parqueaderoapp.R
import com.univalle.parqueaderoapp.ui.componet.botones.card.LoginCard
import com.univalle.parqueaderoapp.ui.componet.logo.MarcaSplash
import com.univalle.parqueaderoapp.ui.componet.logo.MarcaSuperior
import kotlinx.coroutines.launch


private val SUBIR_MARCA = 120.dp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onIniciarSesionClick: () -> Unit,
    onCrearCuenta: () -> Unit,
    onIniciarInvitado: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.intro_parqueadero)
    )

    val progreso by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    val animacionFinalizo = progreso == 1f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        PrimarioOscuro,
                        IlustracionMoradoOscuro
                    )
                )
            )
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(40.dp))

            LottieAnimation(
                composition = composition,
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            AnimatedVisibility(
                visible = animacionFinalizo,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 450),
                    initialOffsetY = { alturaTotal -> alturaTotal },
                ) + expandVertically(animationSpec = tween(durationMillis = 450)) + fadeIn(),
            ) {
                LoginCard(
                    onIniciarSesionClick = onIniciarSesionClick,
                    onGoogleClick = {
                        coroutineScope.launch {
                            try {
                                val credentialManager = CredentialManager.create(context)

                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId("888285203701-2gh4l69mauuca89s2q8045ejrii0vrsu.apps.googleusercontent.com")
                                    .setAutoSelectEnabled(true)
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                val result = credentialManager.getCredential(context, request)

                                val credential = result.credential
                                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                    // Mandamos el token a Firebase a través del ViewModel
                                    viewModel.loginConGoogle(googleIdTokenCredential.idToken)
                                }
                            } catch (e: Exception) {
                                Log.e("AuthGoogle", "Error o cancelado por usuario: ${e.message}")
                            }
                        }
                    },
                    onCrearCuentaClick = onCrearCuenta,
                    onInvitadoClick = onIniciarInvitado,
                )
            }
        }


        AnimatedVisibility(
            visible = !animacionFinalizo,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = -SUBIR_MARCA),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)),
        ) {
            MarcaSplash()
        }

        // Marca pequeña de la esquina: aparece cuando termina la animación.
        AnimatedVisibility(
            visible = animacionFinalizo,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 24.dp, top = 16.dp),
            enter = fadeIn(animationSpec = tween(durationMillis = 400)),
        ) {
            MarcaSuperior()
        }
    }
}