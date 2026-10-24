package com.univalle.parqueaderoapp.ui.vehiculo.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.BordeSuave
import co.parktulua.core.designsystem.Disponible
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro

@Composable
fun PlacaInputBoxes(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int = 6,
    esValida: Boolean = false
) {
    val tieneError = value.length == length && !esValida

    val offsetX = remember { Animatable(0f) }

    // Cada vez que tenga error, ejecutamos la sacudida rápida
    LaunchedEffect(tieneError) {
        if (tieneError) {
            offsetX.animateTo(6f, animationSpec = tween(40))
            offsetX.animateTo(-6f, animationSpec = tween(40))
            offsetX.animateTo(4f, animationSpec = tween(40))
            offsetX.animateTo(-4f, animationSpec = tween(40))
            offsetX.animateTo(0f, animationSpec = tween(40))
        }
    }
    BasicTextField(
        value = value,
        onValueChange = {

            if (it.length <= length) {
                onValueChange(it.uppercase())
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x=offsetX.value.dp),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(length) { index ->
                    val char = when {
                        index < value.length -> value[index].toString()
                        else -> ""
                    }
                    val isActive = index == value.length

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .background(
                                color = if (isActive) PrimarioClaro.copy(alpha = 0.5f) else Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isActive) 1.5.dp else 1.dp,
                                color = when {
                                    esValida -> Disponible
                                    tieneError -> Color.Red
                                    isActive -> Primario
                                    else -> BordeSuave
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutro900,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}