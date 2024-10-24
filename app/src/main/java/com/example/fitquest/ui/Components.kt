package com.example.fitquest.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitquest.AuthState
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent

val verticalGradientBrush = Brush.verticalGradient(
    listOf( Color.Transparent, dark),
    startY = 0f,
    endY = Float.POSITIVE_INFINITY
)
@Composable
fun FitQuestTxt(
    fontSize: TextUnit
    // DEFAUT 68.sp
) {
    Text(
        text =
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = brightOrange)) {
                append("Fit")
            }
            withStyle(style = SpanStyle(color = grayWhite)) {
                append("Quest!")
            }
        },
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment= Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = grayWhite,
            textAlign = TextAlign.Left,
            fontSize = 21.sp
        )

        Text(
            text = "*",
            color = Color.Red,
            fontSize = 21.sp
        )
    }
    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(

        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = brightOrange,
            unfocusedPlaceholderColor = brightOrange,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = grayWhite,
            containerColor = darker,
            unfocusedBorderColor = Color.Transparent,
            focusedSupportingTextColor = brightOrange
        ),
        shape = RoundedCornerShape(size = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
    )

    Spacer(modifier = Modifier.height(30.dp))
}


@Composable
fun Title01(
    label: String,
) {
    Box(modifier = Modifier
        .fillMaxWidth()

    ){
        Text(text = label, color = grayWhite, textAlign = TextAlign.Left, fontSize = 16.sp )
    }
}



//



@Composable
fun OrangeFilledButton(
    label: String,
    onClickFunction: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClickFunction,
        colors = ButtonDefaults.buttonColors(containerColor = brightOrange),
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(65.dp),
        shape = RoundedCornerShape(size = 18.dp)
    ) {
        Text(
            text = label,
            color = grayWhite,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}



@Composable
fun HollowOrangeButton(
    label: String,
    onClickFunction: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClickFunction,
        colors = ButtonDefaults.buttonColors(containerColor = transparent),
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(size = 25.dp),
        border = BorderStroke(4.5.dp, brightOrange)
    ) {
        Text(text = label, color = brightOrange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }

}





//@Composable
//fun FitQuestBackground(function: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(verticalGradientBrush)
//            .padding(30.dp)
//        ,
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ){}
//}