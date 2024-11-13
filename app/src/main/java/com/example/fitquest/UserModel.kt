package com.example.fitquest

////////////////////////////////////Code: Juan, Alexis, Joseph, and Tanner////////////////////////////////////

data class Weekly(
    val name: String = "",
    val workout1: String = "",
    val workout2: String = "",
    val workout3: String = "",
    val Sets: String = "",
    var strength: Int = 0,
    var agility: Int = 0,
    var consistency: Int = 0,
    var stamina: Int = 0,
    var dexterity: Int = 0,
    var flexcoins: Int = 0,
)

data class Workout(
    val name: String = "",
    val description: String = "",
    var strength: Int = 0,
    var agility: Int = 0,
    var consistency: Int = 0,
    var stamina: Int = 0,
    var dexterity: Int = 0,
    var flexcoins: Int = 0,
    )

data class UserProfile(
    val username: String = "",
    var flexcoins: Int = 0,
    var currentAvatar: Int = 0,
    var currentBackground:Int = 0,
    var currentBorder:Int = 0,
    val userStats: UserStats = UserStats(),
    val streak: UserStreak = UserStreak(),
    var lastWorkout: String = "",
    var workoutCount: Int = 0,
    var challenges:Challenge = Challenge()
)

data class Challenge(
    val dailyChallenge: dailyChallenge = dailyChallenge(),
    val weeklyChallenge: weeklyChallenge = weeklyChallenge()
)

data class dailyChallenge(
    var workout1:Workout = Workout(),
    var completeWorkout1:Boolean = false,
    var workout2:Workout = Workout(),
    var completeWorkout2:Boolean = false,
    var workout3:Workout = Workout(),
    var completeWorkout3:Boolean = false,
    var lastUpdate:String = ""
)

data class weeklyChallenge(
    var workout1:Weekly = Weekly(),
    var completeWorkout1:Boolean = false,
    var workout2:Weekly = Weekly(),
    var completeWorkout2:Boolean = false,
    var workout3:Weekly = Weekly(),
    var completeWorkout3:Boolean = false,
    var lastUpdate:String = ""
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
    var longestStreak:Int = 0,
    var lastUpdate: String = ""
)


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

fun updateFlexcoins(user: UserProfile) {
    user.flexcoins += 5
}

fun updateLastWorkout(user: UserProfile, today:String) {
    if(user.lastWorkout != today){
        user.lastWorkout = today
        user.workoutCount = 0
    }
    user.workoutCount++
}

