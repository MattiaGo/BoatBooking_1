package com.example.boatbooking_1


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.Transformations

class LoginViewModel : ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = Transformations.map(FirebaseUserLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}
