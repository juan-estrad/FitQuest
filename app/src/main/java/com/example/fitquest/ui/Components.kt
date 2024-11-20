@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitquest.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.R
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darkOrange
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.example.fitquest.ui.theme.transparent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import androidx.compose.ui.res.painterResource as painterResource1

//////////////////////////////////////Code: Nick, Juan, Joseph and Alexis/////////////////////////////////////

@Composable
fun verticalGradientBrush(
        colorTOP: Color,
        colorBOTTOM: Color,
        endY: Float = Float.POSITIVE_INFINITY,

): Brush {
    return Brush.verticalGradient(
        colors = listOf(colorTOP, colorBOTTOM),
        startY = 250f,
        endY = endY,
    )
}

@Composable
fun horizontalGradientBrush(color1: Color, color2: Color): Brush {
    return Brush.horizontalGradient(
        colors = listOf(color1, color2),
        startX = 250f,
        endX = Float.POSITIVE_INFINITY,
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
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.toFloat()
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
            .height( (screenHeight/14) .dp)
    )
    Spacer(modifier = Modifier.height(15.dp))
}


@Composable
fun requiredTitle01(
    label: String,
    fontSize: Float = 21f,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment= Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = grayWhite,
            textAlign = TextAlign.Left,
            fontSize = fontSize.sp
        )

        Text(
            text = "*",
            color = Color.Red,
            fontSize = fontSize.sp
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField3(
    isNumber: Boolean = true,
    placeholder: String,
    value: String,
    width: Dp,
    textAlign: TextAlign = TextAlign.Center,
    fontSize: Float,
    fontColor: Color = grayWhite,
    placeHolderFontSize : Float = fontSize,
    placeHolderFontStyle: FontStyle = FontStyle.Normal,
    onValueChange: (String) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.toFloat()
    var text by remember { mutableStateOf(value)}
    OutlinedTextField(
        value =value,
        onValueChange = {
            if (isNumber) {
                if (it.all { char -> char.isDigit() })  {

                    val strippedText = it.trimStart { char -> char == '0' }
                    text = strippedText
                    onValueChange(strippedText)


                }
            }
            else{
                onValueChange(it)
            }
        },
        singleLine = true,
        keyboardOptions =
        if (isNumber) {
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        }
        else {
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = brightOrange,
            containerColor = darker,
            unfocusedBorderColor = dark,

            ),
        shape = RoundedCornerShape(size = 20.dp),
        textStyle = LocalTextStyle.current.copy(
            fontSize = fontSize.sp,
            color = grayWhite,
            textAlign = textAlign
        ),
        placeholder = {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = fontColor,
                    textAlign = textAlign,
                    fontSize = placeHolderFontSize.sp,
                    style = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontStyle = placeHolderFontStyle
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = Modifier
            .height(screenHeightDp.dp/12)
            .width(width)
    )
}


@Composable
fun Title01_LEFT(
    label: String,
    color: Color,
    fontSize: Float
) {
    Box(modifier = Modifier
        .fillMaxWidth()
    ){
        Text(
            text = label,
            color = color,
            textAlign = TextAlign.Left,
            fontSize = fontSize.sp )
    }
}

@Composable
fun OrangeFilledButton(
    label: String,
    onClickFunction: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth().height(65.dp)
) {
    Button(
        onClick = onClickFunction,
        colors = ButtonDefaults.buttonColors(containerColor = brightOrange),
        enabled = enabled,
        modifier = modifier,
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
fun OrangeFilledButton2(
    label: String,
    onClickFunction: () -> Unit,
    enabled: Boolean,
    fontSize: Float,
    modifier: Modifier = Modifier.fillMaxWidth().height(65.dp),
    buttomShapeRoundess: Float = 18f
) {
    Button(
        onClick = onClickFunction,
        colors = ButtonDefaults.buttonColors(containerColor = brightOrange),
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(size = buttomShapeRoundess.dp)
    ) {
        Text(
            text = label,
            color = grayWhite,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LOGBUTTON(
    label: String,
    onClickFunction: () -> Unit,
    enabled: Boolean,
    fontSize: Float,
    screenHeightDp: Float = 40f,
    modifier: Modifier = Modifier
        .background( horizontalGradientBrush(dark, brightOrange), RoundedCornerShape(size = 40.dp))
        .fillMaxWidth()
        .height((screenHeightDp.dp / 9f))
        .border(
            (5.3f).dp,
            brush = horizontalGradientBrush(darkOrange, dark),
            RoundedCornerShape(size = 40.dp)
        )
    ,
    buttomShapeRoundess: Float = 18f
) {
    Button(
        onClick = onClickFunction,
        colors = ButtonDefaults.buttonColors(containerColor = brightOrange),
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(size = buttomShapeRoundess.dp)
    ) {
        Text(
            text = label,
            color = grayWhite,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold
        )
    }
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
fun ClickableImageWithText(
    label: String,
    contentDescription: String,
    onClickFunction: () -> Unit,
    enabled: Boolean,
    imageID: Int
){
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeightDp/6.4).dp)
            .clickable(
                enabled = enabled,
                onClick = onClickFunction
            )
            .border(7.dp, brightOrange, RoundedCornerShape(30.dp))
            .padding(0.dp)
            .clip(RoundedCornerShape(30.dp))
    ) {
        Image(
            painter = painterResource1(id = imageID),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(1.15f)
        )
        Text(
            text = label,
            color = grayWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(23.dp)
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun TopAndBottomAppBar(
    topBar: Boolean = true,
    contents: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userRef = database.getReference("Users").child(id)
                    userRef.get().addOnSuccessListener { dataSnapshot ->
                        userProfile = dataSnapshot.getValue(UserProfile::class.java)
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
    val systembarPadding = WindowInsets.systemBars.asPaddingValues()
    Scaffold(
        topBar = {
            userProfile?.let { profile ->
                val bitmap: ImageBitmap = ImageBitmap.imageResource(id = userProfile!!.currentBackground)
                Box(
                    modifier = Modifier
                        .height(systembarPadding.calculateTopPadding())
                        .fillMaxWidth()
                        .background(horizontalGradientBrush(dark, dark))
                )
                if(topBar){
                    Box(
                        modifier = modifier
                            .height((screenHeightDp / 7).dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Canvas(modifier = Modifier
                            .fillMaxSize()
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            drawImage(
                                image = bitmap,
                                srcSize = IntSize( bitmap.width, bitmap.width/3),
                                dstSize = IntSize(canvasWidth.toInt(), canvasHeight.toInt()),
                                srcOffset = IntOffset.Zero,
                                dstOffset =  IntOffset.Zero
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size((screenHeightDp / 8.5).dp)
                                .clip(CircleShape)
                                .background(verticalGradientBrush(darker, dark))
                            ,
                            contentAlignment = Alignment.Center

                        ) {
                            Image(
                                painter = painterResource1(id = userProfile!!.currentAvatar),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize(),
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center

                        ) {
                            Image(
                                painter = painterResource1(id = userProfile!!.currentBorder),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .align(Alignment.BottomCenter),
                            ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp)
                                    .align(Alignment.BottomCenter),
                                ) {
                            }
                            Row {
                                Box(modifier = Modifier.size(36.dp))
                                {
                                    Image(
                                        painter = painterResource1(id = R.drawable.flexcoin),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Text(
                                    text =
                                    buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = grayWhite,
                                                fontSize = (screenWidthDp / 15f).sp,
                                                fontWeight = FontWeight.Bold,

                                                )
                                        ) {
                                            append(" " +profile.flexcoins.toString())
                                        }
                                    },
                                    textAlign = TextAlign.Left,
                                    style = androidx.compose.ui.text.TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            blurRadius = 8f,
                                            offset = Offset(2f, 2f)
                                        )
                                    )
                                )
                            }
                        }


                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(x = 0.dp, y = 37.dp)
                                .padding(8.dp)
                                .align(Alignment.BottomCenter),
                            ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp)
                                    .align(Alignment.BottomCenter),

                                ) {
                            }

                            Row {
                                Text(
                                    text =
                                    buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = grayWhite,
                                                fontSize = (screenWidthDp / 15f).sp,
                                                fontWeight = FontWeight.Bold,
                                                )
                                        ) {
                                            append("\uD83D\uDD25")
                                        }

                                        withStyle(
                                            style = SpanStyle(
                                                color = grayWhite,
                                                fontSize = (screenWidthDp / 15f).sp,
                                                fontWeight = FontWeight.Bold,
                                                )
                                        ) {
                                            append(" " + profile.streak.streak.toString() + " DAYS")
                                        }
                                    },
                                    textAlign = TextAlign.Left,
                                    style = androidx.compose.ui.text.TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            blurRadius = 8f,
                                            offset = Offset(2f, 2f)
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(screenHeightDp.dp / 9f),
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
                                { navController.navigate("settings") },
                                Icons.Filled.Settings,
                                "Settings Page",
                                9.5f
                            )
                        }
                    }
                }
            )
        }
    ){paddingValues ->
        val topPadding =
            if (topBar) {
                paddingValues.calculateTopPadding() - systembarPadding.calculateTopPadding()
            }
            else {
                paddingValues.calculateTopPadding()
            }
        val bottomPadding =
            if (topBar) {
                screenHeightDp.dp / 9f
            }
            else {
                screenHeightDp.dp / 9f
            }
        Box(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    top = topPadding,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = bottomPadding,
                    )
                .fillMaxSize()
                .background(verticalGradientBrush(transparent, dark))
        ){
            contents()
        }
    }
}