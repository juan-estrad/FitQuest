package com.example.fitquest.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.Log
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.Title01_LEFT
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.horizontalGradientBrush
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.verticalGradientBrush
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Enter Focus") }
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthDp = configuration.screenWidthDp
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var avatarCategories by remember { mutableStateOf(listOf<String>()) }
    var backgroundCategories by remember { mutableStateOf(listOf<String>()) }
    var bordersCategories by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users").child(id)
                    //val database =
                    //    Firebase.database.reference.child("inventory") // points to the Users node in firebase

                    val avatar = userRef.child("inventory").child("avatar")
                    val background = userRef.child("inventory").child("background")
                    val borders = userRef.child("inventory").child("borders")

                    userRef.get()
                        .addOnSuccessListener { dataSnapshot ->     //sends a request to retrieve info in firebase
                            userProfile = dataSnapshot.getValue(UserProfile::class.java)
                            //converts the info into a user profile object
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Failed to retrieve user data",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    avatar.get().addOnSuccessListener { dataSnapshot ->
                        val categories = mutableListOf<String>()
                        dataSnapshot.children.forEach {
                            it.value?.let { value -> categories.add(value.toString()) }
                        }
                        avatarCategories = categories
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch profile categories", Toast.LENGTH_SHORT)
                            .show()
                    }

                    background.get().addOnSuccessListener { dataSnapshot ->
                        val categories = mutableListOf<String>()
                        dataSnapshot.children.forEach {
                            it.key?.let { key -> categories.add(key) }
                        }
                        backgroundCategories = categories
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch profile categories", Toast.LENGTH_SHORT)
                            .show()
                    }

                    borders.get().addOnSuccessListener { dataSnapshot ->
                        val categories = mutableListOf<String>()
                        dataSnapshot.children.forEach {
                            it.key?.let { key -> categories.add(key) }
                        }
                        bordersCategories = categories
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch profile categories", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            else -> Unit
        }
    }

    userProfile?.let { profile ->
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

            Box(
                modifier = Modifier

                    .fillMaxWidth(),
                contentAlignment = Alignment.Center


            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,

                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(

                        value = selectedText,
                        onValueChange = { },
                        singleLine = true,
                        readOnly = true,
                        trailingIcon =
                        {
                            Icon(
                                imageVector =
                                if (expanded) {
                                    Icons.Filled.KeyboardArrowDown
                                } else {
                                    Icons.Filled.KeyboardArrowLeft
                                },
                                contentDescription = null,
                                //                                        tint = LocalContentColor.current.copy(alpha = if (expanded) ContentAlpha.high else ContentAlpha.medium),
                                modifier = Modifier.size(50.dp)
                            )// Adjust the size as needed
                        },

                        colors = TextFieldDefaults.outlinedTextFieldColors(

                            focusedBorderColor = brightOrange,
                            containerColor = darker,
                            unfocusedBorderColor = dark,

                            ),
                        shape = RoundedCornerShape(size = 20.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = (screenHeightDp / 30).sp, // Change this to your desired text size
                            fontStyle = FontStyle.Italic,
                            color = dark

                        ),

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .height((screenHeightDp / 12).dp)
                    )
                    ExposedDropdownMenu(
                        modifier = Modifier
                            .border(
                                (2.3f).dp,
                                brush = horizontalGradientBrush(grayWhite, brightOrange),
                                RoundedCornerShape(size = 20.dp)
                            )
                            .background(
                                color = darker,
                                shape = RoundedCornerShape(size = 10.dp)
                            )
                        //                                    .clip(RoundedCornerShape(size = 26.dp))
                        ,
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    )
                    {
                        avatarCategories.forEach { category ->

                            Box(
                                modifier = Modifier
                                    .height((screenHeightDp / 15f).dp)
                                    .border(1.dp, dark, RoundedCornerShape(size = 5.dp))
                                //                                            .background(transparent, RoundedCornerShape(size = 26.dp))
                                //                                                    .padding(30.dp)

                                ,
                                contentAlignment = Alignment.CenterStart

                            ) {
                                //                                        Spacer(modifier = Modifier.height(10.dp))
                                DropdownMenuItem(
                                    //                                        modifier = Modifier.padding(30.dp),
                                    text = {
                                        //                                            Text(text = category)

                                        Title01_LEFT(
                                            "    $category",
                                            grayWhite,
                                            (screenHeightDp / 28f)
                                        )
                                    },
                                    onClick = {
                                        selectedText = category
                                        expanded = false
                                        Toast.makeText(
                                            context,
                                            category,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()

                                    },
                                )
                            }
                        }

                    }
                }


            }

        }
    }
}
