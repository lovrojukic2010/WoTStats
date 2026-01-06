package com.example.wotstats.view.navigation

sealed class Screen(val route: String) {
    data object SignInScreen : Screen("sign_in")
    data object HomeScreen : Screen("home")
    data object FavoritesScreen : Screen("favorites")
    data object ComparisonScreen : Screen("comparison/{firstTankId}/{secondTankId}") {
        fun createRoute(firstTankId: Int, secondTankId: Int) =
            "comparison/$firstTankId/$secondTankId"
    }

    data object VehicleDetailScreen : Screen("vehicle_detail/{tankId}") {
        fun createRoute(tankId: Int) = "vehicle_detail/$tankId"
    }
}