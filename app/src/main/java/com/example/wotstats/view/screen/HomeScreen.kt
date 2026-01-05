package com.example.wotstats.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wotstats.R
import com.example.wotstats.authentication.UserData
import com.example.wotstats.view.components.common.StatusBar
import com.example.wotstats.view.components.home.BottomNavButton
import com.example.wotstats.view.components.home.FiltersArea
import com.example.wotstats.view.components.home.TanksList
import com.example.wotstats.viewmodel.VehiclesViewModel

@Composable
fun HomeScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController
) {
    val vehiclesViewModel: VehiclesViewModel = viewModel()
    val uiState = vehiclesViewModel.uiState.collectAsState().value

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(475.dp)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    TanksList(
                        viewModel = vehiclesViewModel
                    )
                }

                FiltersArea(
                    selectedTier = uiState.selectedTier,
                    selectedNation = uiState.selectedNation,
                    onTierSelected = { tier ->
                        vehiclesViewModel.setFilters(
                            tier = tier,
                            nation = uiState.selectedNation
                        )
                    },
                    onNationSelected = { nation ->
                        vehiclesViewModel.setFilters(
                            tier = uiState.selectedTier,
                            nation = nation
                        )
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomNavButton(
                    text = "TANKS",
                    selected = true
                )
                BottomNavButton(
                    text = "FAVORITES",
                    selected = false
                )
            }
        }
    }
}