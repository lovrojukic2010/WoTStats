package com.example.wotstats.view.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wotstats.R
import com.example.wotstats.authentication.GoogleAuthClient
import com.example.wotstats.extension.NetworkChecker
import com.example.wotstats.view.navigation.Screen
import com.example.wotstats.viewmodel.SignInViewModel
import com.example.wotstats.viewmodel.VehiclesViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val applicationContext = LocalContext.current
    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
        )
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SignInScreen.route) {
        composable(Screen.SignInScreen.route) {
            val signInViewModel: SignInViewModel = viewModel()
            val state by signInViewModel.state.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthClient.getSignedInUser() != null) {
                    navController.navigate(Screen.HomeScreen.route)
                }
            }

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        R.string.sign_in_success,
                        Toast.LENGTH_SHORT,
                    ).show()
                    navController.navigate(Screen.HomeScreen.route)
                    signInViewModel.resetState()
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthClient.signInWithIntent(
                                intent = result.data ?: return@launch,
                            )
                            signInViewModel.onSignInResult(signInResult)
                        }
                    }
                },
            )

            SignInScreen(
                state = state,
                onSignInClick = {
                    if (!NetworkChecker.isNetworkAvailable(applicationContext)) {
                        Toast.makeText(
                            applicationContext,
                            R.string.network_error,
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        coroutineScope.launch {
                            val signInIntentSender = googleAuthClient.signIn(applicationContext)
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch,
                                ).build(),
                            )
                        }
                    }
                },
            )
        }

        composable(Screen.HomeScreen.route) {
            val coroutineScope = rememberCoroutineScope()
            HomeScreen(
                userData = googleAuthClient.getSignedInUser(),
                onSignOut = {
                    coroutineScope.launch {
                        googleAuthClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            R.string.sign_out_success,
                            Toast.LENGTH_SHORT,
                        ).show()
                        navController.navigate(Screen.SignInScreen.route) {
                            popUpTo(Screen.SignInScreen.route)
                        }
                    }
                },
                navController = navController,
            )
        }

        composable(Screen.FavoritesScreen.route) {
            val vehiclesViewModel: VehiclesViewModel = viewModel()
            val coroutineScope = rememberCoroutineScope()
            FavoritesScreen(
                userData = googleAuthClient.getSignedInUser(),
                onSignOut = {
                    coroutineScope.launch {
                        googleAuthClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            R.string.sign_out_success,
                            Toast.LENGTH_SHORT,
                        ).show()
                        navController.navigate(Screen.SignInScreen.route) {
                            popUpTo(Screen.SignInScreen.route)
                        }
                    }
                },
                navController = navController,
                vehiclesViewModel = vehiclesViewModel
            )
        }

        composable(
            route = Screen.ComparisonScreen.route,
            arguments = listOf(
                navArgument("firstTankId") { type = NavType.IntType },
                navArgument("secondTankId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val firstTankId = backStackEntry.arguments?.getInt("firstTankId") ?: 0
            val secondTankId = backStackEntry.arguments?.getInt("secondTankId") ?: 0
            val coroutineScope = rememberCoroutineScope()
            ComparisonScreen(
                userData = googleAuthClient.getSignedInUser(),
                onSignOut = {
                    coroutineScope.launch {
                        googleAuthClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            R.string.sign_out_success,
                            Toast.LENGTH_SHORT,
                        ).show()
                        navController.navigate(Screen.SignInScreen.route) {
                            popUpTo(Screen.SignInScreen.route)
                        }
                    }
                },
                navController = navController,
                firstTankId = firstTankId,
                secondTankId = secondTankId
            )
        }

        composable(
            route = Screen.VehicleDetailScreen.route,
            arguments = listOf(
                navArgument("tankId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val tankId = backStackEntry.arguments?.getInt("tankId") ?: 0
            val coroutineScope = rememberCoroutineScope()
            VehicleDetailScreen(
                userData = googleAuthClient.getSignedInUser(),
                onSignOut = {
                    coroutineScope.launch {
                        googleAuthClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            R.string.sign_out_success,
                            Toast.LENGTH_SHORT,
                        ).show()
                        navController.navigate(Screen.SignInScreen.route) {
                            popUpTo(Screen.SignInScreen.route)
                        }
                    }
                },
                navController = navController,
                tankId = tankId
            )
        }
    }
}