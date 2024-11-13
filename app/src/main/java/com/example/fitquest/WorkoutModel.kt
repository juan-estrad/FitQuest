package com.example.fitquest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fitquest.pages.userID
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

class WorkoutViewModel : ViewModel() {
    //private val database = FirebaseDatabase.getInstance()
    private val database = Firebase.database
    val userID = FirebaseAuth.getInstance().uid

    // Observables for UI
    var todayWorkout = mutableStateOf<Workout?>(null)
    val userStats = mutableStateOf(UserStats())
    val workout:Workout? = Workout()

    private var workoutLoaded = false
    private var lastLoadDate: LocalDate? = null

    fun loadUserChallenges(profile: UserProfile){
        //userProfile = profile
        println("loadUserChallenges called")
        //println(todayWorkout)
        if(profile.challenges.dailyChallenge.lastUpdate != LocalDate.now().format(DateTimeFormatter.ISO_DATE).toString()) {
            //profile.challenges.workout1 = loadRandomWorkout(profile)
            //println("workout1: " + profile.challenges.workout1)
            //profile.challenges.workout1 = todayWorkout.value!!
            //loadRandomWorkout(profile)
            //println("workout2: " + todayWorkout)
            //profile.challenges.workout2 = todayWorkout.value!!
            //loadRandomWorkout(profile)
            //println("workout3: " + todayWorkout)
            //profile.challenges.workout3 = todayWorkout.value!!
            loadRandomWorkout1(profile)
            loadRandomWorkout2(profile)
            loadRandomWorkout3(profile)
            profile.challenges.dailyChallenge.lastUpdate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

            database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("lastUpdate")
                .setValue(profile.challenges.dailyChallenge.lastUpdate)

        }
    }
    // Load a random workout
    fun loadRandomWorkout1(profile: UserProfile){
        println("loadRandomWorkout called")
        println("still in1")
        val workoutsRef = database.getReference("workouts")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            println("still in2")
            if (snapshot.exists()) {
                println("still in3")
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    // Select a random body location
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key

                    // Select a random workout within the body location
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        // Retrieve the workout details, including the name from the key
                        val workoutName = workoutKey ?: "Unknown Workout" // Fallback for safety
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                // Include the workout name when setting today's workout
                                if (workout != null) {
                                    println("workout: $workout")
                                    todayWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.dailyChallenge.workout1 = todayWorkout.value!!
                                    profile.challenges.dailyChallenge.completeWorkout1 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("workout1")
                                        .setValue(profile.challenges.dailyChallenge.workout1)
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout1").setValue(false)
                                    workoutLoaded = true
                                    //lastLoadDate = today // Update last load date

                                }
                            }
                            .addOnFailureListener { error ->
                                // Handle error while fetching workout
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            // Handle error while fetching body locations
            println("Error fetching body locations: ${error.message}")
        }
    }
    fun loadRandomWorkout2(profile: UserProfile){
        println("loadRandomWorkout called")
        println("still in1")
        val workoutsRef = database.getReference("workouts")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            println("still in2")
            if (snapshot.exists()) {
                println("still in3")
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    // Select a random body location
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key

                    // Select a random workout within the body location
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        // Retrieve the workout details, including the name from the key
                        val workoutName = workoutKey ?: "Unknown Workout" // Fallback for safety
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                // Include the workout name when setting today's workout
                                if (workout != null) {
                                    println("workout: $workout")
                                    todayWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.dailyChallenge.workout2 = todayWorkout.value!!
                                    profile.challenges.dailyChallenge.completeWorkout2 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("workout2")
                                        .setValue(profile.challenges.dailyChallenge.workout2)
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout2").setValue(false)
                                    workoutLoaded = true
                                    //lastLoadDate = today // Update last load date

                                }
                            }
                            .addOnFailureListener { error ->
                                // Handle error while fetching workout
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            // Handle error while fetching body locations
            println("Error fetching body locations: ${error.message}")
        }
    }
    fun loadRandomWorkout3(profile: UserProfile){
        println("loadRandomWorkout called")
        println("still in1")
        val workoutsRef = database.getReference("workouts")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            println("still in2")
            if (snapshot.exists()) {
                println("still in3")
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    // Select a random body location
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key

                    // Select a random workout within the body location
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        // Retrieve the workout details, including the name from the key
                        val workoutName = workoutKey ?: "Unknown Workout" // Fallback for safety
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                // Include the workout name when setting today's workout
                                if (workout != null) {
                                    println("workout: $workout")
                                    todayWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.dailyChallenge.workout3 = todayWorkout.value!!
                                    profile.challenges.dailyChallenge.completeWorkout3 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout3").setValue(false)
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("workout3")
                                        .setValue(profile.challenges.dailyChallenge.workout3)
                                    workoutLoaded = true
                                    //lastLoadDate = today // Update last load date

                                }
                            }
                            .addOnFailureListener { error ->
                                // Handle error while fetching workout
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            // Handle error while fetching body locations
            println("Error fetching body locations: ${error.message}")
        }
    }


    private fun loadUserStats() {
        database.getReference("Users").child("$userID").child("userStats").get().addOnSuccessListener { snapshot ->
            userStats.value = snapshot.getValue(UserStats::class.java) ?: UserStats()
        }.addOnFailureListener { error ->
            // Handle error while fetching user stats
            println("Error fetching user stats: ${error.message}")
        }
    }

    fun completeWorkout(profile: UserProfile) {
        todayWorkout.value?.let { workout ->
            userStats.value = userStats.value.copy(
                strength = userStats.value.strength + workout.strength,
                agility = userStats.value.agility + workout.agility,
                dexterity = userStats.value.dexterity + workout.dexterity,
                consistency = userStats.value.consistency + workout.consistency,
                stamina = userStats.value.stamina + workout.stamina,

                )
//            profile.value = profile.value.copy(
//                flexcoins = profile.value.flexcoins + workout.flexcoins,
//
//                )

            profile.flexcoins += workout.flexcoins

            // Update the user's stats in Firebase
            database.getReference("Users").child("$userID").child("userStats").setValue(userStats.value)
                .addOnFailureListener { error ->
                    // Handle error while updating user stats
                    println("Error updating user stats: ${error.message}")
                }

            database.getReference("Users").child("$userID").child("flexcoins").setValue(profile.flexcoins)
                .addOnFailureListener { error ->
                    // Handle error while updating user stats
                    println("Error updating user stats: ${error.message}")
                }
        }
    }

    init {
        loadUserStats()  // Load user stats on ViewModel init
    }
}

class WeeklyWorkoutViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    //val database2 = Firebase.database
    val userID = FirebaseAuth.getInstance().uid

    // Observables for UI
    var weeklyWorkout = mutableStateOf<Workout?>(null)
    val userStats = mutableStateOf(UserStats())
    val workout:Workout? = Workout()

    private var workoutLoaded = false
    private var lastLoadDate: LocalDate? = null

    fun loadUserChallenges(profile: UserProfile){
        //userProfile = profile
        println("loadUserChallenges called")
        val currentWeekStart = LocalDate.now().with(DayOfWeek.SUNDAY).format(DateTimeFormatter.ISO_DATE)

        //println(todayWorkout)
        if(profile.challenges.weeklyChallenge.lastUpdate != currentWeekStart) {
            //profile.challenges.workout1 = loadRandomWorkout(profile)
            //println("workout1: " + profile.challenges.workout1)
            //profile.challenges.workout1 = todayWorkout.value!!
            //loadRandomWorkout(profile)
            //println("workout2: " + todayWorkout)
            //profile.challenges.workout2 = todayWorkout.value!!
            //loadRandomWorkout(profile)
            //println("workout3: " + todayWorkout)
            //profile.challenges.workout3 = todayWorkout.value!!
            loadRandomWorkout1(profile)
            loadRandomWorkout2(profile)
            loadRandomWorkout3(profile)
            profile.challenges.weeklyChallenge.lastUpdate = currentWeekStart.toString()

            database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("lastUpdate")
                .setValue(profile.challenges.weeklyChallenge.lastUpdate)

        }
    }
    // Load a random workout
    fun loadRandomWorkout1(profile: UserProfile){
        println("loadRandomWorkout called")
        println("still in1")
        val workoutsRef = database.getReference("workouts")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            println("still in2")
            if (snapshot.exists()) {
                println("still in3")
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    // Select a random body location
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key

                    // Select a random workout within the body location
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        // Retrieve the workout details, including the name from the key
                        val workoutName = workoutKey ?: "Unknown Workout" // Fallback for safety
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                // Include the workout name when setting today's workout
                                if (workout != null) {
                                    println("workout: $workout")
                                    weeklyWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.weeklyChallenge.workout1 = weeklyWorkout.value!!
                                    profile.challenges.weeklyChallenge.completeWorkout1 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout1").setValue(false)
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("workout1")
                                        .setValue(profile.challenges.weeklyChallenge.workout1)
                                    workoutLoaded = true
                                    //lastLoadDate = today // Update last load date

                                }
                            }
                            .addOnFailureListener { error ->
                                // Handle error while fetching workout
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            // Handle error while fetching body locations
            println("Error fetching body locations: ${error.message}")
        }
    }
    fun loadRandomWorkout2(profile: UserProfile){
        println("loadRandomWorkout called")
        println("still in1")
        val workoutsRef = database.getReference("workouts")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            println("still in2")
            if (snapshot.exists()) {
                println("still in3")
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    // Select a random body location
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key

                    // Select a random workout within the body location
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        // Retrieve the workout details, including the name from the key
                        val workoutName = workoutKey ?: "Unknown Workout" // Fallback for safety
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                // Include the workout name when setting today's workout
                                if (workout != null) {
                                    println("workout: $workout")
                                    weeklyWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.weeklyChallenge.workout2 = weeklyWorkout.value!!
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("workout2")
                                        .setValue(profile.challenges.weeklyChallenge.workout2)
                                    profile.challenges.weeklyChallenge.completeWorkout2 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout2").setValue(false)
                                    workoutLoaded = true
                                    //lastLoadDate = today // Update last load date

                                }
                            }
                            .addOnFailureListener { error ->
                                // Handle error while fetching workout
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            // Handle error while fetching body locations
            println("Error fetching body locations: ${error.message}")
        }
    }
    fun loadRandomWorkout3(profile: UserProfile){
        println("loadRandomWorkout called")
        println("still in1")
        val workoutsRef = database.getReference("workouts")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            println("still in2")
            if (snapshot.exists()) {
                println("still in3")
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    // Select a random body location
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key

                    // Select a random workout within the body location
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        // Retrieve the workout details, including the name from the key
                        val workoutName = workoutKey ?: "Unknown Workout" // Fallback for safety
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                // Include the workout name when setting today's workout
                                if (workout != null) {
                                    println("workout: $workout")
                                    weeklyWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.weeklyChallenge.workout3 = weeklyWorkout.value!!
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("workout3")
                                        .setValue(profile.challenges.weeklyChallenge.workout3)
                                    profile.challenges.weeklyChallenge.completeWorkout3 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout3").setValue(false)
                                    workoutLoaded = true
                                    //lastLoadDate = today // Update last load date

                                }
                            }
                            .addOnFailureListener { error ->
                                // Handle error while fetching workout
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            // Handle error while fetching body locations
            println("Error fetching body locations: ${error.message}")
        }
    }


    private fun loadUserStats() {
        database.getReference("Users").child("$userID").child("userStats").get().addOnSuccessListener { snapshot ->
            userStats.value = snapshot.getValue(UserStats::class.java) ?: UserStats()
        }.addOnFailureListener { error ->
            // Handle error while fetching user stats
            println("Error fetching user stats: ${error.message}")
        }
    }

    fun completeWorkout(profile: UserProfile) {
        weeklyWorkout.value?.let { workout ->
            userStats.value = userStats.value.copy(
                strength = userStats.value.strength + workout.strength,
                agility = userStats.value.agility + workout.agility,
                dexterity = userStats.value.dexterity + workout.dexterity,
                consistency = userStats.value.consistency + workout.consistency,
                stamina = userStats.value.stamina + workout.stamina,

                )
//            profile.value = profile.value.copy(
//                flexcoins = profile.value.flexcoins + workout.flexcoins,
//
//                )

            profile.flexcoins += workout.flexcoins

            // Update the user's stats in Firebase
            database.getReference("Users").child("$userID").child("userStats").setValue(userStats.value)
                .addOnFailureListener { error ->
                    // Handle error while updating user stats
                    println("Error updating user stats: ${error.message}")
                }

            database.getReference("Users").child("$userID").child("flexcoins").setValue(profile.flexcoins)
                .addOnFailureListener { error ->
                    // Handle error while updating user stats
                    println("Error updating user stats: ${error.message}")
                }
        }
    }

    init {
        loadUserStats()  // Load user stats on ViewModel init
    }
}