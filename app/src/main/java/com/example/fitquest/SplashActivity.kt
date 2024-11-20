package com.example.fitquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fitquest.ui.theme.FitQuestTheme

////////////////////////////////////Code: Nick and Juan////////////////////////////////////

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val authViewModel : AuthViewModel by viewModels()
        val workoutViewModel : WorkoutViewModel by viewModels()
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            FitQuestTheme {
                Scaffold(modifier = Modifier.fillMaxSize())
                { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel,
                        workoutViewModel = workoutViewModel,
                        weeklyWorkoutViewModel = WeeklyWorkoutViewModel()
                    )
                }
            }
        }
    }
}



