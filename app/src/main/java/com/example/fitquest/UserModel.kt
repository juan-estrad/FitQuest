package com.example.fitquest

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
    //val inventory:Inventory = Inventory(),
    val userStats: UserStats = UserStats(),
    //val logging: Logging = Logging(),
    val streak: UserStreak = UserStreak(),
    var lastWorkout: String = "",
    var workoutCount: Int = 0
)

//data class Inventory(
//    var avatar:Avatar = Avatar(),
//    var background:Background = Background(),
//    var borders:Borders = Borders()
//)

//data class Avatar(
//    var default:Int = R.drawable.avatar
//)
//data class Background(
//    var default:Int = R.drawable.background_1
//)
//data class Borders(
//    var default:Int = R.drawable.border
//)

data class ID(
    val name:String = "",
    val desc:String = ""
)

data class UserStats(
    var agility: Int = 0,
    var consistency: Int = 0,
    var dexterity: Int = 0,
    var stamina: Int = 0,
    var strength: Int = 0
)
data class PieData(
    val label: String,
    val value: Float,
    val color: androidx.compose.ui.graphics.Color
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

