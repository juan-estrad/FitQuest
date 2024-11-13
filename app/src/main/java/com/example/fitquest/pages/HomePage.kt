package com.example.fitquest.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.R
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.ClickableImageWithText
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darkOrange
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.example.fitquest.ui.verticalGradientBrush
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import kotlin.system.exitProcess



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    TopAndBottomAppBar(
    contents = { HomePageContents(modifier,navController,authViewModel) },
    modifier = modifier,
    navController = navController,
    authViewModel = authViewModel
    )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid



    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users")
                        .child(id) // points to the Users node in firebase

                    userRef.get()
                        .addOnSuccessListener { dataSnapshot ->     //sends a request to retrieve info in firebase
                            userProfile =
                                dataSnapshot.getValue(UserProfile::class.java) //converts the info into a user profile object
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to retrieve user data", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }

            else -> Unit
        }
    }


    // display content if the userProfile is not null
    userProfile?.let { profile ->


        Column(
            modifier = Modifier
                .fillMaxSize()
//                .background(verticalGradientBrush(transparent, dark))
                .padding(
                    start = 20.dp,
//                    top = screenHeightDp.dp / 7 - 15.dp,
//                    top = paddingValues.calculateTopPadding(),
                    top = 0.dp,
                    end = 20.dp,
                    bottom = 0.dp
                )
                .verticalScroll(rememberScrollState())
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {




            val database = Firebase.database
            val myRef = database.getReference("Users")



            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.toFloat()

            Spacer(
                modifier = Modifier
                    .height( (screenHeight/36) .dp)
            )

            //LOG WORK OUT PAGE
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // STATS PAGE
                ClickableImageWithText(
                    "Log Workout",
                    "Stats page Background",
                    {navController.navigate("logging")},
                    authState.value != AuthState.Loading,
                    R.drawable.logpagebtn
                )

            }

            Spacer(modifier = Modifier.height(23.dp))

            //STORE PAGE
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // STATS PAGE
                ClickableImageWithText(
                    "Store",
                    "Stats page Background",
                    { navController.navigate("store") },
                    authState.value != AuthState.Loading,
                    R.drawable.storepagebtn
                )

            }

            Spacer(modifier = Modifier.height(23.dp))

            //STATS PAGE
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                ClickableImageWithText(
                    "Profile",
                    "Stats page Background",
                    {navController.navigate("stats")},
                    authState.value != AuthState.Loading,
                    R.drawable.statspagebtn
                )

            }

            Spacer(modifier = Modifier.height(26.dp))

            // FYP BUTTON
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                ClickableImageWithText(
                    "For You",
                    "Recommended Work Outs image",
                    { navController.navigate("foryou") },
                    authState.value != AuthState.Loading,
                    R.drawable.fypbtn
                )

            }

            Spacer(
                modifier = Modifier
                    .height( (screenHeight/32) .dp)
            )


            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
//                    .background(darker)
                    ,
                    Arrangement.Start
            ){
                TextButton(

                    onClick = { authViewModel.signout() }) {
                    Text(
                        text = "Sign Out",
                        color = darkOrange,
                        textAlign = TextAlign.Left
                    )
                }
            }


            Spacer(
                modifier = Modifier
                    .height( (screenHeight/36) .dp)
            )


        }
    }


    // This modify the device's back button to function like a the device's home button
    // pressing it will make it minimize the app in stead of shutting it down completely
    BackHandler(enabled = true ) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)


        // THIS FUNCTION BELOW WILL SHUTDOWN THE APP
        // which the app has to be relaunch after

        //exitProcess(1);
    }
}

