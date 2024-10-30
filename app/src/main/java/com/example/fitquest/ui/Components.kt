@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitquest.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.BeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


    @Composable
fun verticalGradientBrush(): Brush {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.toFloat()

    return Brush.verticalGradient(
        colors = listOf(transparent, dark),
        startY = 250f,
        endY = Float.POSITIVE_INFINITY,
    )
}


@Composable
fun horizontalGradientBrush(): Brush {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.toFloat()

    return Brush.horizontalGradient(
        colors = listOf(transparent, dark),
        startX = 250f,
        endX = Float.POSITIVE_INFINITY,
    )
}


//val verticalGradientBrush = Brush.verticalGradient(
//    listOf( Color.Transparent, dark),
//    startY = 500f,
//    endY = 2000f
//)


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
                append("Quest")
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
    Spacer(modifier = Modifier.height(3.dp))

    OutlinedTextField(

        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = brightOrange,
            containerColor = darker,
            unfocusedBorderColor = Color.Transparent,

        ),
        shape = RoundedCornerShape(size = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
    )

    Spacer(modifier = Modifier.height(15.dp))
}


@Composable
fun Title01(
    label: String,
    color: Color,
    fontSize: Float
) {
    Box(modifier = Modifier
        .fillMaxWidth()

    ){
        Text(text = label, color = color, textAlign = TextAlign.Left, fontSize = fontSize.sp )
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





@Composable
fun NavigationBarItem(
    onClickFunction: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    screenWidthDivedBy: Float
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthDp = configuration.screenWidthDp

    IconButton(
        onClick = onClickFunction,
        modifier = Modifier.size(screenWidthDp.dp / 5),
    ) {
        Icon(
            imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(screenWidthDp.dp / screenWidthDivedBy),
            tint = brightOrange,
        )
    }
}





@ExperimentalMaterial3Api
@Composable
fun TopAndBottomAppBar(
    contents: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel

){

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
                    val userRef = database.getReference("Users").child(id) // points to the Users node in firebase

                    userRef.get().addOnSuccessListener { dataSnapshot ->     //sends a request to retrieve info in firebase
                        userProfile = dataSnapshot.getValue(UserProfile::class.java) //converts the info into a user profile object
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> Unit
        }
    }




    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthDp = configuration.screenWidthDp


    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = transparent,
//                    titleContentColor = grayWhite,
                )
                ,
                modifier = Modifier
//                    .size(screenHeightDp.dp / 3)
                    .fillMaxWidth()
//                    .height(screenHeightDp.dp / 6)
                    .padding(0.dp)
                    .background(grayWhite)
                ,


//                title = {},
                //THIS IS TO FILL THE TOP CAR CONTENT
                title = {
                    Box(
                        modifier = modifier

                            .fillMaxSize()
//                            .height(screenHeightDp.dp / 2)
//                            .fillMaxWidth()
//                            .aspectRatio(1f)
                            .background(darker)
                    )
                    {}
//                    Column(
//                        modifier = modifier
//                            .fillMaxSize()
//                            .fillMaxHeight()
//                            .background(com.example.fitquest.ui.theme.verticalGradientBrush)
////                            .padding(0.dp),
////                            horizontalAlignment = AbsoluteAlignment.Left,
//
//
//                    ) {
//
//                        // This displays the streak
//                        // i think the future plan is to have a fire emoji or something around it
//
//                        userProfile?.let { profile ->
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                modifier = Modifier
////                                    .height(screenHeightDp.dp / 5)
////                                    .fillMaxSize()
//                                    .fillMaxHeight()
//                                    .padding(0.dp)
////                                    .background(grayWhite)
//                                ,
//
//                            ) {
//                                Text(
//                                    text = "\uD83D\uDD25 STREAK:",
//                                    color = grayWhite,
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 18.sp
//                                )
//
//                                Text(
//                                    text = userProfile?.let
//                                    { profile ->
//                                        profile.streak.streak.toString()
//
//                                    } ?: "Loading Streaks...",
//                                    color = grayWhite,
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//
//                                Box(
//                                    modifier = Modifier
//                                        .size(100.dp)
//                                        .aspectRatio(1f)
//                                        .clip(CircleShape)
//                                        .background(Color.Gray),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Text(
//                                        profile.username,
//                                        fontSize = 20.sp,
//                                        color = Color.White
//                                    ) //profile username
//                                }
//                            }
//                        }
//                    }
                }
                // Additional configurations
            )
        },
        bottomBar = {
            BottomAppBar(

                modifier = Modifier.height(screenHeightDp.dp / 10f),
                containerColor = darker,
                actions = {
                    Column(

                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
//                            navigationItems.forEach { navigationItem -> navigationItem() }

                            NavigationBarItem(
                                { navController.navigate("home") },
                                Icons.Filled.Home,
                                "Store Page",
                                9.5f
                            )

                            NavigationBarItem(
                                { navController.navigate("store") },
                                Icons.Filled.ShoppingCart,
                                "Store Page",
                                9.5f
                            )

                            NavigationBarItem(
                                { navController.navigate("logging") },
                                Icons.Filled.AddCircle,
                                "Store Page",
                                4.5f
                            )

                            NavigationBarItem(
                                { navController.navigate("stats") },
                                Icons.Filled.AccountCircle,
                                "Store Page",
                                9.5f
                            )

                            NavigationBarItem(
                                { navController.navigate("stats") },
                                Icons.Filled.Settings,
                                "Store Page",
                                9.5f
                            )


                        }
                    }
                }
            )
//
        }

    ){paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        top = screenHeightDp.dp / 7 ,
//                        top = paddingValues.calculateTopPadding(),
//                        top = 0.dp,

                        end = paddingValues.calculateEndPadding(LayoutDirection.Rtl),
                        bottom = screenHeightDp.dp / 13f,
                    )
                .fillMaxSize()
                .background(verticalGradientBrush())
        ){
            contents();
        }


    }

}