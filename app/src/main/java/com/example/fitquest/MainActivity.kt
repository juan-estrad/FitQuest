package com.example.fitquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitquest.pages.WorkoutScreen
import com.example.fitquest.ui.theme.FitQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel : AuthViewModel by viewModels()
        val splashScreen = installSplashScreen()
//        splashScreen.setKeepOnScreenCondition{true};
        enableEdgeToEdge()

        setContent {
            val workoutViewModel: WorkoutViewModel = viewModel()
            WorkoutScreen(viewModel = workoutViewModel)
//            FitQuestTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MyAppNavigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel)
//                }
//            }
        }
    }
}



