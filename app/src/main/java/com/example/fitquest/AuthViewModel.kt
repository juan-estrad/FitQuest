package com.example.fitquest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import kotlin.concurrent.timerTask

class AuthViewModel : ViewModel(){

    private val _content = mutableStateOf<List<MyContent>>(emptyList())
    val content: State<List<MyContent>> = _content
    private var lastLoadedDate: LocalDate? = null
    fun loadContent() {
        val currentDate = LocalDate.now()

        // Check if we need to load new content
        if (lastLoadedDate != currentDate) {
            viewModelScope.launch {
                // Simulate fetching data (e.g., from a database or API)
                val fetchedContent = fetchDailyContent()
                _content.value = fetchedContent
                lastLoadedDate = currentDate // Update the last loaded date
            }
        }
    }
    private fun fetchDailyContent(): List<MyContent> {
        // Simulate fetching data. Replace with your actual data source.
        return listOf(
            MyContent(1, "Daily Task 1", "Description for task 1"),
            MyContent(2, "Daily Task 2", "Description for task 2")
        )
    }


    private val auth : FirebaseAuth =FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init{
        checkAuthStatus()
    }


    fun checkAuthStatus(){
       if(auth.currentUser == null){
           _authState.value = AuthState.Unauthenticated
       }
        else{
            _authState.value = AuthState.Authenticated
       }
    }

    fun login(email:String, password : String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }
                else
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went Wrong")

            }
    }

    fun signup(email:String, password : String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }
                else
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went Wrong")

            }
    }


    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }


}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading : AuthState()
    data class Error(val message : String): AuthState()


}
