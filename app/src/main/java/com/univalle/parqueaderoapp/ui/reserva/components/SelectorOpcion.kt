package com.univalle.parqueaderoapp.ui.reserva.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.BordeSuave
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.Radios

@Composable
fun SelectorOpcion(
    text : String,
    isSelected : Boolean,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
){
    Box(
        modifier = Modifier
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Primario.copy(alpha = 0.1f) else Neutro0)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Primario else BordeSuave,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable{onClick()},
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Primario else Neutro900 ,
            modifier = Modifier.padding(horizontal = 16.dp) //
        )
    }

}
