package com.example.fitquest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

////////////////////////////////////Code: Juan and Alexis////////////////////////////////////

class WorkoutViewModel : ViewModel() {
    private val database = Firebase.database
    private val userID = FirebaseAuth.getInstance().uid
    var todayWorkout = mutableStateOf<Workout?>(null)
    private val userStats = mutableStateOf(UserStats())

    private var workoutLoaded = false

    fun loadUserChallenges(profile: UserProfile){
        println("loadUserChallenges called")
        if(profile.challenges.dailyChallenge.lastUpdate != LocalDate.now().format(DateTimeFormatter.ISO_DATE).toString()) {
            loadRandomWorkout1(profile)
            loadRandomWorkout2(profile)
            loadRandomWorkout3(profile)
            profile.challenges.dailyChallenge.lastUpdate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

            database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("lastUpdate")
                .setValue(profile.challenges.dailyChallenge.lastUpdate)
        }
    }

    private fun loadRandomWorkout1(profile: UserProfile){
        val workoutsRef = database.getReference("workouts")
        workoutsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        val workoutName = workoutKey ?: "Unknown Workout"
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                if (workout != null) {
                                    println("workout: $workout")
                                    todayWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.dailyChallenge.workout1 = todayWorkout.value!!
                                    profile.challenges.dailyChallenge.completeWorkout1 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("workout1")
                                        .setValue(profile.challenges.dailyChallenge.workout1)
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout1").setValue(false)
                                    workoutLoaded = true
                                }
                            }
                            .addOnFailureListener { error ->
                                println("Error fetching workout: ${error.message}")
                            }
                    }
                }
            }
        }.addOnFailureListener { error ->
            println("Error fetching body locations: ${error.message}")
        }
    }
    private fun loadRandomWorkout2(profile: UserProfile){
        val workoutsRef = database.getReference("workouts")
        workoutsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        val workoutName = workoutKey ?: "Unknown Workout"
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                if (workout != null) {
                                    println("workout: $workout")
                                    todayWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.dailyChallenge.workout2 = todayWorkout.value!!
                                    profile.challenges.dailyChallenge.completeWorkout2 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("workout2")
                                        .setValue(profile.challenges.dailyChallenge.workout2)
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout2").setValue(false)
                                    workoutLoaded = true
                                }
                            }
                            .addOnFailureListener { error ->
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            println("Error fetching body locations: ${error.message}")
        }
    }
    private fun loadRandomWorkout3(profile: UserProfile){
        val workoutsRef = database.getReference("workouts")
        workoutsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key
                    val workouts = randomBodyLocation.children.toList()
                    if (workouts.isNotEmpty()) {
                        val randomWorkout = workouts.random()
                        val workoutKey = randomWorkout.key
                        val workoutName = workoutKey ?: "Unknown Workout" // Fallback for safety
                        database.getReference("workouts/$bodyLocationKey/$workoutKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Workout::class.java)
                                if (workout != null) {
                                    println("workout: $workout")
                                    todayWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.dailyChallenge.workout3 = todayWorkout.value!!
                                    profile.challenges.dailyChallenge.completeWorkout3 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("completeWorkout3").setValue(false)
                                    database.getReference("Users").child("$userID").child("challenges").child("dailyChallenge").child("workout3")
                                        .setValue(profile.challenges.dailyChallenge.workout3)
                                    workoutLoaded = true
                                }
                            }
                            .addOnFailureListener { error ->
                                println("Error fetching workout: ${error.message}")
                            }

                    }
                }
            }
        }.addOnFailureListener { error ->
            println("Error fetching body locations: ${error.message}")
        }
    }


    private fun loadUserStats() {
        database.getReference("Users").child("$userID").child("userStats").get().addOnSuccessListener { snapshot ->
            userStats.value = snapshot.getValue(UserStats::class.java) ?: UserStats()
        }.addOnFailureListener { error ->
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

            profile.flexcoins += workout.flexcoins

            database.getReference("Users").child("$userID").child("userStats").setValue(userStats.value)
                .addOnFailureListener { error ->
                    println("Error updating user stats: ${error.message}")
                }

            database.getReference("Users").child("$userID").child("flexcoins").setValue(profile.flexcoins)
                .addOnFailureListener { error ->
                    println("Error updating user stats: ${error.message}")
                }
        }
    }

    init {
        loadUserStats()
    }
}

class WeeklyWorkoutViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val userID = FirebaseAuth.getInstance().uid
    var weeklyWorkout = mutableStateOf<Weekly?>(null)
    private val userStats = mutableStateOf(UserStats())

    private var workoutLoaded = false

    fun loadUserChallenges(profile: UserProfile){
        val currentWeekStart = LocalDate.now().with(DayOfWeek.SUNDAY).format(DateTimeFormatter.ISO_DATE)
        if(profile.challenges.weeklyChallenge.lastUpdate != currentWeekStart) {
            loadRandomWorkout1(profile)
            loadRandomWorkout2(profile)
            loadRandomWorkout3(profile)
            profile.challenges.weeklyChallenge.lastUpdate = currentWeekStart.toString()

            database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("lastUpdate")
                .setValue(profile.challenges.weeklyChallenge.lastUpdate)
        }
    }

    private fun loadRandomWorkout1(profile: UserProfile){
        val workoutsRef = database.getReference("Weekly")
        workoutsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key
                        val workoutName = bodyLocationKey ?: "Unknown Workout"
                        database.getReference("Weekly/$bodyLocationKey")
                            .get().addOnSuccessListener { workoutSnapshot ->
                                val workout = workoutSnapshot.getValue(Weekly::class.java)
                                if (workout != null) {
                                    weeklyWorkout.value = workout.copy(name = workoutName)
                                    profile.challenges.weeklyChallenge.workout1 = weeklyWorkout.value!!
                                    profile.challenges.weeklyChallenge.completeWorkout1 = false
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout1").setValue(false)
                                    database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("workout1")
                                        .setValue(profile.challenges.weeklyChallenge.workout1)
                                    workoutLoaded = true

                                }
                            }
                            .addOnFailureListener { error ->
                                // Handle error while fetching workout
                                println("Error fetching workout: ${error.message}")
                            }
                }
            }
        }.addOnFailureListener { error ->
            println("Error fetching body locations: ${error.message}")
        }
    }

    private fun loadRandomWorkout2(profile: UserProfile){
        val workoutsRef = database.getReference("Weekly")
        workoutsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key
                    val workoutName = bodyLocationKey ?: "Unknown Workout"
                    database.getReference("Weekly/$bodyLocationKey")
                        .get().addOnSuccessListener { workoutSnapshot ->
                            val workout = workoutSnapshot.getValue(Weekly::class.java)
                            if (workout != null) {
                                weeklyWorkout.value = workout.copy(name = workoutName)
                                profile.challenges.weeklyChallenge.workout2 = weeklyWorkout.value!!
                                database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("workout2")
                                    .setValue(profile.challenges.weeklyChallenge.workout2)
                                profile.challenges.weeklyChallenge.completeWorkout2 = false
                                database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout2").setValue(false)
                                workoutLoaded = true
                            }
                        }
                        .addOnFailureListener { error ->
                            println("Error fetching workout: ${error.message}")
                        }

                }
            }
        }.addOnFailureListener { error ->
            println("Error fetching body locations: ${error.message}")
        }
    }

    private fun loadRandomWorkout3(profile: UserProfile){
        val workoutsRef = database.getReference("Weekly")
        workoutsRef.get().addOnSuccessListener { snapshot ->
            println("still in2")
            if (snapshot.exists()) {
                println("still in3")
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key
                    val workoutName = bodyLocationKey ?: "Unknown Workout"
                    database.getReference("Weekly/$bodyLocationKey")
                        .get().addOnSuccessListener { workoutSnapshot ->
                            val workout = workoutSnapshot.getValue(Weekly::class.java)
                            if (workout != null) {
                                println("workout: $workout")
                                weeklyWorkout.value = workout.copy(name = workoutName)
                                profile.challenges.weeklyChallenge.workout3 = weeklyWorkout.value!!
                                database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("workout3")
                                    .setValue(profile.challenges.weeklyChallenge.workout3)
                                profile.challenges.weeklyChallenge.completeWorkout3 = false
                                database.getReference("Users").child("$userID").child("challenges").child("weeklyChallenge").child("completeWorkout3").setValue(false)
                                workoutLoaded = true

                            }
                        }
                        .addOnFailureListener { error ->
                            println("Error fetching workout: ${error.message}")
                        }
                }
            }
        }.addOnFailureListener { error ->
            println("Error fetching body locations: ${error.message}")
        }
    }


    private fun loadUserStats() {
        database.getReference("Users").child("$userID").child("userStats").get().addOnSuccessListener { snapshot ->
            userStats.value = snapshot.getValue(UserStats::class.java) ?: UserStats()
        }.addOnFailureListener { error ->
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

            profile.flexcoins += workout.flexcoins

            database.getReference("Users").child("$userID").child("userStats").setValue(userStats.value)
                .addOnFailureListener { error ->
                    println("Error updating user stats: ${error.message}")
                }

            database.getReference("Users").child("$userID").child("flexcoins").setValue(profile.flexcoins)
                .addOnFailureListener { error ->
                    println("Error updating user stats: ${error.message}")
                }
        }
    }

    init {
        loadUserStats()
    }
}