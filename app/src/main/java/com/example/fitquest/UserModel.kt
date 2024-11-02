package com.example.fitquest

import java.time.LocalDateTime

data class Workout(
    val name: String = "",
    val description: String = "",
    var strength: Int = 0,
)

data class UserProfile(
    val username: String = "",
    val flexcoins: Int = 0,
    val userStats: UserStats = UserStats(),
    val logging: Logging = Logging(),
    val streak: UserStreak = UserStreak() //Need to add to DB to work
)

data class UserStats(
    var agility: Int = 0,
    var consistency: Int = 0,
    var dexterity: Int = 0,
    var stamina: Int = 0,
    var strength: Int = 0
)

data class UserStreak(
    var streak:Int = 0,
    var lastUpdate: LocalDateTime? = LocalDateTime.now(),
    var streakExpiration:LocalDateTime? = lastUpdate?.plusDays(2)
)

data class Logging(
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

data class Log(
    val workout: String = "",
    val type: String = "",
    val sets: Int = 0,
    val reps: Int = 0,
    val weight: String = "",
    val workouttime: String = ""
)

fun isStreakExpired(user:UserProfile):Boolean{
    return user.streak.lastUpdate!!.isAfter(user.streak.streakExpiration)
}

fun updateStreak(user:UserProfile){
    if(isStreakExpired(user)){
        user.streak.streak = 0
    } else {
        user.streak.streak++
        user.streak.lastUpdate = LocalDateTime.now()
        user.streak.streakExpiration = user.streak.lastUpdate?.plusDays(2)
    }
}