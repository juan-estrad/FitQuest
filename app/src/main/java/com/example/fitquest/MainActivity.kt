package com.example.fitquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitquest.pages.WorkoutScreen
import com.example.fitquest.WorkoutViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: WorkoutViewModel = viewModel()
            MainScreen(viewModel)
//            FitQuestTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MyAppNavigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel)
//                }
//            }
        }
        val authViewModel : AuthViewModel by viewModels()
        val splashScreen = installSplashScreen()
//        splashScreen.setKeepOnScreenCondition{true};
        enableEdgeToEdge()
    }
}

@Composable
fun MainScreen(viewModel: WorkoutViewModel) {
    // Now you can use the viewModel instance here
    WorkoutScreen(viewModel = viewModel) // Pass the ViewModel to your WorkoutScreen
}



