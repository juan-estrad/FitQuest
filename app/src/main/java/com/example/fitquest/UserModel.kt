package com.example.fitquest

data class UserProfile(
    val username: String = "",
    val flexcoins: Int = 0,
    val streak: Int = 0,
    val userStats: UserStats = UserStats(),
    val logging: Logging = Logging()
)

data class UserStats(
    val agility: Int = 0,
    val consistency: Int = 0,
    val dexterity: Int = 0,
    val stamina: Int = 0,
    val strength: Int = 0
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


