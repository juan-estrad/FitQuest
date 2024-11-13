package com.example.fitquest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitquest.pages.CustomizePage
import com.example.fitquest.pages.HomePage
import com.example.fitquest.pages.LoginPage
import com.example.fitquest.pages.SignupPage
import com.example.fitquest.pages.LoggingPage
import com.example.fitquest.pages.StatsPage
import com.example.fitquest.pages.ForYouPage
import com.example.fitquest.pages.SettingsPage
import com.example.fitquest.pages.SplashScreen
import com.example.fitquest.pages.StorePage

////////////////////////////////////Code: Joseph////////////////////////////////////

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, workoutViewModel: WorkoutViewModel, weeklyWorkoutViewModel: WeeklyWorkoutViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash",
        builder = {
            composable("splash"){
                SplashScreen(modifier,navController,authViewModel)
            }
            composable("login"){
                LoginPage(modifier,navController,authViewModel)
            }
            composable("signup") {
                SignupPage(modifier, navController, authViewModel)
            }
            composable("home") {
                HomePage(modifier, navController, authViewModel)
            }
            composable("logging") {
                LoggingPage(modifier, navController, authViewModel)
            }
            composable("stats") {
                StatsPage(modifier, navController, authViewModel)
            }
            composable("foryou") {
                ForYouPage(modifier, navController, authViewModel, workoutViewModel, weeklyWorkoutViewModel)
            }
            composable("store") {
                StorePage(modifier, navController, authViewModel)
            }
            composable("settings"){
                SettingsPage(modifier, navController, authViewModel)
            }
            composable("customize"){
                CustomizePage(modifier, navController, authViewModel)
            }
        }
    )
}

