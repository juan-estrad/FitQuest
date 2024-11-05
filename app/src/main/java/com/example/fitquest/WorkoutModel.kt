package com.example.fitquest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fitquest.pages.userID
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

class WorkoutViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()

    // Observables for UI
    val todayWorkout = mutableStateOf<Workout?>(null)
    val userStats = mutableStateOf(UserStats())
    val userProfile = mutableStateOf(UserProfile())

    private var workoutLoaded = false
    private var lastLoadDate: LocalDate? = null


    // Load a random workout
    fun loadRandomWorkout() {
        val today = LocalDate.now()

        if (workoutLoaded && lastLoadDate == today) {
            return
        }

        val workoutsRef = database.getReference("workouts")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
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
                                    todayWorkout.value = workout.copy(name = workoutName)
                                    workoutLoaded = true
                                    lastLoadDate = today // Update last load date

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

    fun completeWorkout() {
        todayWorkout.value?.let { workout ->
            userStats.value = userStats.value.copy(
                strength = userStats.value.strength + workout.strength,
                agility = userStats.value.agility + workout.agility,
                dexterity = userStats.value.dexterity + workout.dexterity,
                consistency = userStats.value.consistency + workout.consistency,
                stamina = userStats.value.stamina + workout.stamina,

                )
            userProfile.value = userProfile.value.copy(
                flexcoins = userProfile.value.flexcoins + workout.flexcoins,

                )

            // Update the user's stats in Firebase
            database.getReference("Users").child("$userID").child("userStats").setValue(userStats.value)
                .addOnFailureListener { error ->
                    // Handle error while updating user stats
                    println("Error updating user stats: ${error.message}")
                }

            database.getReference("Users").child("$userID").child("flexcoins").setValue(userProfile.value.flexcoins)
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

    // Observables for UI
    val weeklyWorkout = mutableStateOf<Weekly?>(null)
    val weeklyuserStats = mutableStateOf(UserStats())
    val weeklyuserProfile = mutableStateOf(UserProfile())

    private var workoutLoaded = false
    private var lastLoadWeek: Int? = null


    // Load a random workout
    fun loadRandomWorkout() {
        val currentWeek = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

        if (workoutLoaded && lastLoadWeek == currentWeek) {
            return
        }

        val workoutsRef = database.getReference("Weekly")

        workoutsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bodyLocations = snapshot.children.toList()
                if (bodyLocations.isNotEmpty()) {
                    // Select a random body location
                    val randomBodyLocation = bodyLocations.random()
                    val bodyLocationKey = randomBodyLocation.key

                    // Retrieve the workout details, including the name from the key
                    val workoutName = bodyLocationKey ?: "Unknown Workout" // Fallback for safety
                    database.getReference("Weekly/$bodyLocationKey")
                        .get().addOnSuccessListener { workoutSnapshot ->
                            val workout = workoutSnapshot.getValue(Weekly::class.java)
                            // Include the workout name when setting today's workout
                            if (workout != null) {
                                weeklyWorkout.value = workout.copy(name = workoutName)
                                workoutLoaded = true
                                lastLoadWeek = currentWeek // Update last load week

                            }
                        }
                        .addOnFailureListener { error ->
                            // Handle error while fetching workout
                            println("Error fetching workout: ${error.message}")
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
            weeklyuserStats.value = snapshot.getValue(UserStats::class.java) ?: UserStats()
        }.addOnFailureListener { error ->
            // Handle error while fetching user stats
            println("Error fetching user stats: ${error.message}")
        }
    }

    fun completeWorkout() {
        weeklyWorkout.value?.let { weekly ->
            weeklyuserStats.value = weeklyuserStats.value.copy(
                strength = weeklyuserStats.value.strength + weekly.strength,
                agility = weeklyuserStats.value.agility + weekly.agility,
                dexterity = weeklyuserStats.value.dexterity + weekly.dexterity,
                consistency = weeklyuserStats.value.consistency + weekly.consistency,
                stamina = weeklyuserStats.value.stamina + weekly.stamina,

                )
            weeklyuserProfile.value = weeklyuserProfile.value.copy(
                flexcoins = weeklyuserProfile.value.flexcoins + weekly.flexcoins,

                )

            // Update the user's stats in Firebase
            database.getReference("Users").child("$userID").child("userStats").setValue(weeklyuserStats.value)
                .addOnFailureListener { error ->
                    // Handle error while updating user stats
                    println("Error updating user stats: ${error.message}")
                }

            database.getReference("Users").child("$userID").child("flexcoins").setValue(weeklyuserProfile.value.flexcoins)
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