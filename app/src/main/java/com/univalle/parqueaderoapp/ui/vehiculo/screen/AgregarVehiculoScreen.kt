package com.univalle.parqueaderoapp.ui.vehiculo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Acento
import co.parktulua.core.designsystem.AcentoClaro
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.LlenoTexto
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioOscuro
import com.univalle.parqueaderoapp.ui.vehiculo.components.PlacaInputBoxes
import com.univalle.parqueaderoapp.ui.vehiculo.components.SelectorTipoVehiculo
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarVehiculoScreen(
    viewModel: AgregarVehiculoViewModel = koinViewModel(),
    onRegistroExitoso: () -> Unit,
    onVolverClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            onRegistroExitoso()
        }
    }

    Scaffold(
        containerColor = FondoApp,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Agregar vehículo",
                            fontWeight = FontWeight.Bold,
                            color = PrimarioOscuro,
                            fontSize = 18.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = PrimarioOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoApp)
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Selector de Tipo de Vehículo Dinámico (con tarjetas e iconos)
            Text(
                text = "Tipo de vehículo",
                fontSize = 14.sp,
                color = Neutro600,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            SelectorTipoVehiculo(
                tipoSeleccionado = uiState.tipo,
                onSeleccionar = { viewModel.onTipoSeleccionado(it) }
            )

            Spacer(modifier = Modifier.height(34.dp))

            // 2. Campo Placa en Casillas Separadas
            if (uiState.mostrarPlaca) {
                Text(
                    text = "Placa del vehículo",
                    fontSize = 14.sp,
                    color = Neutro600,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                PlacaInputBoxes(
                    value = uiState.placa,
                    onValueChange = { viewModel.onPlacaChange(it) },
                    length = 6
                )

            } else {
                Text(
                    text = "Las bicicletas no necesitan placa.",
                    fontSize = 13.sp,
                    color = Neutro600,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 3. Aviso Informativo de Vehículos Compartidos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AcentoClaro, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Si otra persona ya registró esta placa, el vehículo se comparte. Tu historial sigue siendo solo tuyo.",
                    color = Acento,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            // Mostrar mensaje de error si el repositorio falla
            if (uiState.errorMensaje != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.errorMensaje!!,
                    color = LlenoTexto,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Botón de Guardar Vehículo
            Button(
                onClick = { viewModel.onGuardar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primario),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState.puedeGuardar
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(2.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Guardar vehículo", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}