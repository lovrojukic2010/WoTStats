package com.example.wotstats.view.components.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wotstats.api.data.Vehicle
import com.example.wotstats.extension.toRomanTier
import com.example.wotstats.view.navigation.Screen
import com.example.wotstats.viewmodel.FavoritesViewModel

@Composable
fun FavoritesList(
    viewModel: FavoritesViewModel,
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    if (state.tanks.isEmpty() && !state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No vehicles to display",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 0.dp)
        ) {
            itemsIndexed(state.tanks) { _, tank ->
                FavoriteTankRow(
                    tank = tank,
                    onClick = {
                        navController.navigate(
                            Screen.VehicleDetailScreen.createRoute(tank.tankId)
                        )
                    },
                    onRemoveClick = { viewModel.onRemoveFavouriteClicked(it) }
                )
            }
        }
    }
}

@Composable
fun FavoriteTankRow(
    tank: Vehicle,
    onClick: () -> Unit,
    onRemoveClick: (Vehicle) -> Unit
) {
    Row(
        modifier = Modifier
            .width(240.dp)
            .clickable { onClick() }
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .background(
                color = Color.White.copy(alpha = 0.6f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = tank.images?.bigIcon,
            contentDescription = tank.name,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = tank.tier?.toRomanTier().orEmpty(),
            fontSize = 9.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            modifier = Modifier.width(20.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = tank.shortName ?: tank.name.orEmpty(),
            fontSize = 9.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            modifier = Modifier.width(80.dp)
        )

        Spacer(Modifier.weight(1f))

        IconToggleButton(
            checked = true,
            onCheckedChange = { onRemoveClick(tank) }
        ) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Remove from favourites",
                tint = Color.Red
            )
        }
    }
}
