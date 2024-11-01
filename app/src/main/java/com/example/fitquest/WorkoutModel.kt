package com.example.fitquest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fitquest.pages.userID
import com.google.firebase.database.FirebaseDatabase

class WorkoutViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()

    // Observables for UI
    val todayWorkout = mutableStateOf<Workout?>(null)
    val userStats = mutableStateOf(UserStats())
    private var workoutLoaded = false

    // Load a random workout
    fun loadRandomWorkout() {
        if (workoutLoaded) {
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
            )
            // Update the user's stats in Firebase
            database.getReference("Users").child("$userID").child("userStats").setValue(userStats.value)
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