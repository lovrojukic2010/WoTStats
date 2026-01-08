package com.example.wotstats.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wotstats.R
import com.example.wotstats.api.client.WotClient
import com.example.wotstats.api.data.Vehicle
import com.example.wotstats.api.service.WotService
import com.example.wotstats.authentication.UserData
import com.example.wotstats.view.components.common.StatusBar
import com.example.wotstats.view.components.home.BottomNavButton
import com.example.wotstats.view.components.stats.TankStatsCard

@Composable
fun VehicleDetailScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController,
    tankId: Int
) {
    val service = WotService(WotClient.WotService.client)
    var tank by remember { mutableStateOf<Vehicle?>(null) }
    val fields = stringResource(R.string.vehicle_details_fields)
    LaunchedEffect(tankId) {
        tank = service.loadVehicles(
            limit = 1,
            pageNo = 1,
            tankId = listOf(tankId),
            fields = fields
        )[0]
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.home_background),
                contentScale = ContentScale.FillBounds,
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            StatusBar(userData, onSignOut)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TankStatsCard(tank = tank)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomNavButton(
                    text = "TANKS",
                    selected = false,
                    navController
                )
                BottomNavButton(
                    text = "FAVORITES",
                    selected = false,
                    navController
                )
            }
        }
    }
}