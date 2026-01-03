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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wotstats.R
import com.example.wotstats.authentication.GoogleAuthClient
import com.example.wotstats.extension.NetworkChecker
import com.example.wotstats.view.navigation.Screen
import com.example.wotstats.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
            val signInViewModel: SignInViewModel = koinViewModel()
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
                        navController.popBackStack()
                    }
                },
                navController = navController,
            )
        }
    }
}