package com.example.fitquest.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthViewModel
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.verticalGradientBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    TopAndBottomAppBar(
        contents = { CustomizePageContents(modifier, navController, authViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )
}
@Composable
    fun CustomizePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(verticalGradientBrush)
            .padding(16.dp)
    ) {
        Text(
            text = "Customize Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

    }
}
