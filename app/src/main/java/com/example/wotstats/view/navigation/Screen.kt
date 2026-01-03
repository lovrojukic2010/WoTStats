package com.example.wotstats.view.navigation

sealed class Screen(val route: String) {
    data object SignInScreen : Screen("sign_in")
    data object HomeScreen : Screen("home")
}