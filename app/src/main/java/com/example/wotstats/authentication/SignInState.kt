package com.example.wotstats.authentication

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
)