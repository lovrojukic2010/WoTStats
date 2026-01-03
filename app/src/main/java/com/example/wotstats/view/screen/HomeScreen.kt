package com.example.wotstats.view.screen

import android.annotation.TargetApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.wotstats.R
import com.example.wotstats.authentication.UserData
import com.example.wotstats.view.components.StatusBar

@Composable
@TargetApi(29)
fun HomeScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds,
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            StatusBar(userData, onSignOut)
        }
    }
}