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
import com.example.wotstats.view.components.compare.TankCompareCard
import com.example.wotstats.view.components.home.BottomNavButton

@Composable
fun ComparisonScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController,
    firstTankId: Int,
    secondTankId: Int
) {
    val service = WotService(WotClient.WotClientProvider.client)
    var firstTank by remember { mutableStateOf<Vehicle?>(null) }
    var secondTank by remember { mutableStateOf<Vehicle?>(null) }
    val fields = stringResource(R.string.vehicle_details_fields)
    LaunchedEffect(firstTankId, secondTankId) {
        val tanks = service.loadVehicles(
            limit = 2,
            pageNo = 1,
            tankId = listOf(firstTankId, secondTankId),
            fields = fields
        )
        firstTank = tanks[0]
        secondTank = tanks[1]
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
                TankCompareCard(firstTank, secondTank)
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