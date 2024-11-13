package com.example.fitquest.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitquest.AuthState
import com.example.fitquest.AuthViewModel
import com.example.fitquest.R
import com.example.fitquest.UserProfile
import com.example.fitquest.ui.Title01_LEFT
import com.example.fitquest.ui.TopAndBottomAppBar
import com.example.fitquest.ui.theme.brightOrange
import com.example.fitquest.ui.theme.dark
import com.example.fitquest.ui.theme.darker
import com.example.fitquest.ui.theme.grayWhite
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    TopAndBottomAppBar(
        contents = { StorePageContents(modifier,navController,authViewModel) },
        modifier = modifier,
        navController = navController,
        authViewModel = authViewModel
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePageContents(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                userID?.let { id ->
                    val userFlexCoins = userProfile?.flexcoins ?: 0
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

    userProfile?.let { profile ->
        val userFlexCoins = userProfile?.flexcoins ?: 0
        Column(
            modifier = modifier
                .fillMaxSize()
//                .background(Color.DarkGray)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("FitQuest Store", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFCCAA00))
                Spacer(modifier = Modifier.height(12.dp))

                // Backgrounds Category
                Title01_LEFT("Backgrounds", grayWhite, 40f);
                LazyRow {
                    items(1) { index ->
                        BackgroundStoreItemBox(R.drawable.background_1, userFlexCoins, navController, authViewModel, profile)
                        BackgroundStoreItemBox(R.drawable.background_2, userFlexCoins, navController, authViewModel, profile)
                        BackgroundStoreItemBox(R.drawable.background_3, userFlexCoins, navController, authViewModel, profile)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Borders Category
                Title01_LEFT("Borders", grayWhite, 40f);
                LazyRow {
                    items(1) { index ->
                        BordersStoreItemBox(R.drawable.border_1, userFlexCoins, navController, authViewModel, profile)
                        BordersStoreItemBox(R.drawable.border_2, userFlexCoins, navController, authViewModel,profile)
                        BordersStoreItemBox(R.drawable.border_3, userFlexCoins, navController, authViewModel,profile)
                }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Avatars Category
                Title01_LEFT("Avatars", grayWhite, 40f);
                LazyRow {
                    items(1) { index ->
                        AvatarStoreItemBox(R.drawable.profile_1, userFlexCoins, navController, authViewModel, profile)
                        AvatarStoreItemBox(R.drawable.profile_2, userFlexCoins, navController, authViewModel, profile)
                        AvatarStoreItemBox(R.drawable.profile_3, userFlexCoins, navController, authViewModel, profile)
                    }
                }
            }

            // Smaller Home Button at the Bottom
        }
    }
}

@Composable
fun BackgroundStoreItemBox(image: Int, userFlexCoins: Int, navController: NavController, authViewModel: AuthViewModel, profile: UserProfile) {
    println("New background: " + image)
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val showInsufficientFundsDialog = remember { mutableStateOf(false) }
    val itemCost = 100
    var hasPurchased by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthDp = configuration.screenWidthDp
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid

    val checkBack = database.getReference("Users").child("$userID").child("inventory").child("background")
    checkBack.get().addOnSuccessListener { snapshot ->
        if (snapshot.exists()) {
            val background = snapshot.children.toList()
            for (i in background) {
                println("User db background: " + i.key.toString())
                println("Current background: " + image.toString())
                println(i.key.toString() == image.toString())
                if (i.key.toString() == image.toString()) {
                    hasPurchased = true
                }
            }

        }
    }

    Box(
        modifier = Modifier
            .size((screenHeightDp / 8).dp)
            .background(darker, shape = RoundedCornerShape(12.dp))
            .border(3.dp, dark, RoundedCornerShape(12.dp))
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Display image
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // "100 FlexCoins" button

            Button(
                onClick = {
                    if (userFlexCoins >= itemCost) {
                        showConfirmationDialog.value = true
                    } else {
                        showInsufficientFundsDialog.value = true
                    }
                },
                enabled = !hasPurchased,  // Disable if purchased
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasPurchased) Color.Gray else brightOrange  // Grey out when purchased
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("100 FlexCoins", color = Color.Black, fontSize = 12.sp)
            }
        }
    }

    // Confirmation Dialog
    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            title = { Text("Confirm Purchase") },
            text = { Text("Are you sure you want to purchase for 100 FlexCoins?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog.value = false
                        hasPurchased = true  // Set purchased state to true
                        val image2 = database.getReference("Users").child("$userID").child("inventory").child("background")
//                        image2.child("$image").child("name").setValue("test")

                        if (image == R.drawable.background_1){
                            image2.child("$image").setValue("Sunset Background")
                        }
                        else if(image == R.drawable.background_2){
                            image2.child("$image").setValue("Blue Background")
                        }
                        else if(image == R.drawable.background_3){
                            image2.child("$image").setValue("Collesium")
                        }

                        //println(profile.inventory.background.default)
                        //profile.currentBackground = image
                        //println(profile.inventory.background.default)

                        val ref3 = database.getReference("Users").child("$userID")
//                        ref3.child("currentBackground").setValue(image)
                        profile.flexcoins -= itemCost
                        ref3.child("flexcoins").setValue(profile.flexcoins)
                        navController.navigate("store")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB58900))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmationDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    // Insufficient Funds Dialog
    if (showInsufficientFundsDialog.value) {
        AlertDialog(
            onDismissRequest = { showInsufficientFundsDialog.value = false },
            title = { Text("Insufficient FlexCoins") },
            text = { Text("You do not have enough FlexCoins to purchase.") },
            confirmButton = {
                Button(
                    onClick = { showInsufficientFundsDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun BordersStoreItemBox(image: Int, userFlexCoins: Int, navController: NavController, authViewModel: AuthViewModel, profile: UserProfile) {
    println("New Border: " + image)
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val showInsufficientFundsDialog = remember { mutableStateOf(false) }
    val itemCost = 100
    var hasPurchased by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthDp = configuration.screenWidthDp
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid

    val checkBack = database.getReference("Users").child("$userID").child("inventory").child("borders")
    checkBack.get().addOnSuccessListener { snapshot ->
        if (snapshot.exists()) {
            val borders = snapshot.children.toList()
            for (i in borders) {
                println("User db border: " + i.key.toString())
                println("current border:" + image.toString())
                println(i.key.toString() == image.toString())
                if (i.key.toString() == image.toString()) {
                    hasPurchased = true
                }
            }

        }
    }

    Box(
        modifier = Modifier
            .size((screenHeightDp / 8).dp)
            .background(darker, shape = RoundedCornerShape(12.dp))
            .border(3.dp, dark, RoundedCornerShape(12.dp))
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Display image
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // "100 FlexCoins" button

            Button(
                onClick = {
                    if (userFlexCoins >= itemCost) {
                        showConfirmationDialog.value = true
                    } else {
                        showInsufficientFundsDialog.value = true
                    }
                },
                enabled = !hasPurchased,  // Disable if purchased
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasPurchased) Color.Gray else brightOrange  // Grey out when purchased
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("100 FlexCoins", color = Color.Black, fontSize = 12.sp)
            }
        }
    }

    // Confirmation Dialog
    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            title = { Text("Confirm Purchase") },
            text = { Text("Are you sure you want to purchase for 100 FlexCoins?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog.value = false
                        hasPurchased = true  // Set purchased state to true
                        val image2 = database.getReference("Users").child("$userID").child("inventory").child("borders")

                        if(image == R.drawable.border_1){
                            image2.child("$image").setValue("Purple Border")
                        }
                        else if(image == R.drawable.border_2){
                            image2.child("$image").setValue("Gold Border")
                        }
                        else if(image == R.drawable.border_3){
                            image2.child("$image").setValue("Green/Blue Border")
                        }

                        //println(profile.inventory.borders.default)
                        //profile.currentBorder = image
                        //println(profile.inventory.borders.default)

                        val ref3 = database.getReference("Users").child("$userID")
                        //ref3.child("currentBorder").setValue(image)
                        profile.flexcoins -= itemCost
                        ref3.child("flexcoins").setValue(profile.flexcoins)
                        navController.navigate("store")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB58900))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmationDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    // Insufficient Funds Dialog
    if (showInsufficientFundsDialog.value) {
        AlertDialog(
            onDismissRequest = { showInsufficientFundsDialog.value = false },
            title = { Text("Insufficient FlexCoins") },
            text = { Text("You do not have enough FlexCoins to purchase.") },
            confirmButton = {
                Button(
                    onClick = { showInsufficientFundsDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun AvatarStoreItemBox(image: Int, userFlexCoins: Int, navController: NavController, authViewModel: AuthViewModel, profile: UserProfile) {
    println("New Avatar: " + image)
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val showInsufficientFundsDialog = remember { mutableStateOf(false) }
    val itemCost = 100
    var hasPurchased by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidthDp = configuration.screenWidthDp
    val database = Firebase.database //initialize an instance of the realtime database
    val userID = FirebaseAuth.getInstance().uid

    val checkBack = database.getReference("Users").child("$userID").child("inventory").child("avatar")
    checkBack.get().addOnSuccessListener { snapshot ->
        if (snapshot.exists()) {
            val avatar = snapshot.children.toList()
            for (i in avatar) {
                println("User db avatar : " + i.key.toString())
                println("Current avatar: " + image.toString())
                println(i.key.toString() == image.toString())
                if (i.key.toString() == image.toString()) {
                    hasPurchased = true
                }
            }

        }
    }

    Box(
        modifier = Modifier
            .size((screenHeightDp / 8).dp)
            .background(darker, shape = RoundedCornerShape(12.dp))
            .border(3.dp, dark, RoundedCornerShape(12.dp))
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Display image
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // "100 FlexCoins" button

            Button(
                onClick = {
                    if (userFlexCoins >= itemCost) {
                        showConfirmationDialog.value = true
                    } else {
                        showInsufficientFundsDialog.value = true
                    }
                },
                enabled = !hasPurchased,  // Disable if purchased
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasPurchased) Color.Gray else brightOrange  // Grey out when purchased
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("100 FlexCoins", color = Color.Black, fontSize = 12.sp)
            }
        }
    }

    // Confirmation Dialog
    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            title = { Text("Confirm Purchase") },
            text = { Text("Are you sure you want to purchase for 100 FlexCoins?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog.value = false
                        hasPurchased = true  // Set purchased state to true
                        val image2 = database.getReference("Users").child("$userID").child("inventory").child("avatar")
                        if(image == R.drawable.profile_1){
                            image2.child("$image").setValue("Arnold")
                        }
                        else if(image == R.drawable.profile_2){
                            image2.child("$image").setValue("Ronnie")
                        }
                        else if(image == R.drawable.profile_3){
                            image2.child("$image").setValue("David")
                        }
                        //println(profile.inventory.avatar.default)
                        //profile.currentAvatar = image
                        //println(profile.inventory.avatar.default)

                        val ref3 = database.getReference("Users").child("$userID")
                        //ref3.child("currentAvatar").setValue(image)
                        profile.flexcoins -= itemCost
                        ref3.child("flexcoins").setValue(profile.flexcoins)
                        navController.navigate("store")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB58900))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmationDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    // Insufficient Funds Dialog
    if (showInsufficientFundsDialog.value) {
        AlertDialog(
            onDismissRequest = { showInsufficientFundsDialog.value = false },
            title = { Text("Insufficient FlexCoins") },
            text = { Text("You do not have enough FlexCoins to purchase.") },
            confirmButton = {
                Button(
                    onClick = { showInsufficientFundsDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }
}