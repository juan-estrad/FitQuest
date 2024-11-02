package com.example.fitquest

import java.time.LocalDate
import java.time.LocalDateTime

data class MyContent(
    val id: Int,
    val title: String,
    val description: String
)

data class UserProfile(
    val username: String = "",
    val flexcoins: Int = 0,
    val userStats: UserStats = UserStats(),
    //val logging: Logging = Logging(),
    val streak: UserStreak = UserStreak()
)

data class UserStats(
    val agility: Int = 0,
    val consistency: Int = 0,
    val dexterity: Int = 0,
    val stamina: Int = 0,
    val strength: Int = 0
)

data class UserStreak(
    var streak:Int = 0,
    var longestStreak:Int = 0,
    var lastUpdate: String = ""
)

/*data class Logging(
    val date: Date = Date()
)

data class Date(
    val year: Year = Year()
)

data class Year(
    val monthday: Monthday = Monthday()
)

data class Monthday(
    val log: Log = Log()
)
*/
data class Log(
    val workout: String = "",
    val type: String = "",
    val sets: Int = 0,
    val reps: Int = 0,
    val weight: String = "",
    val workouttime: String = ""
)

fun updateStreak(user:UserProfile, today: String, yesterday: String){
    if(user.streak.lastUpdate == yesterday){
        user.streak.streak++
        user.streak.longestStreak = maxOf(user.streak.longestStreak, user.streak.streak)
        user.streak.lastUpdate = today
    } else if (user.streak.lastUpdate != today){
        user.streak.streak = 1
        user.streak.lastUpdate = today
    }
}