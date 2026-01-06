package com.example.wotstats.view.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
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
import coil.compose.AsyncImage
import com.example.wotstats.api.data.Vehicle
import com.example.wotstats.extension.toRomanTier
import com.example.wotstats.viewmodel.VehiclesViewModel

@Composable
fun TanksList(
    viewModel: VehiclesViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(vertical = 0.dp)
    ) {
        itemsIndexed(state.tanks) { index, tank ->
            if (index == state.tanks.lastIndex && !state.isLoading && !state.endReached) {
                viewModel.loadNextPage()
            }

            val id = tank.tankId

            val isInComparison = state.comparisonIds.contains(id)
            val isFavourite = state.favouriteIds.contains(id)

            val comparisonFullAndNotThis =
                state.comparisonIds.size >= 2 && !isInComparison

            TankRow(
                tank = tank,
                isInComparison = isInComparison,
                isFavourite = isFavourite,
                comparisonDisabled = comparisonFullAndNotThis,
                onCompareClick = { viewModel.onCompareClicked(it) },
                onFavouriteClick = { viewModel.onFavouriteClicked(it) }
            )
        }
    }
}

@Composable
fun TankRow(
    tank: Vehicle,
    isInComparison: Boolean,
    isFavourite: Boolean,
    comparisonDisabled: Boolean,
    onCompareClick: (Vehicle) -> Unit,
    onFavouriteClick: (Vehicle) -> Unit,
) {
    Row(
        modifier = Modifier
            .width(220.dp)
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
            modifier = Modifier.width(40.dp)
        )

        Spacer(Modifier.weight(1f))

        if (!comparisonDisabled) {
            IconToggleButton(
                modifier = Modifier.width(50.dp),
                checked = isInComparison,
                onCheckedChange = { onCompareClick(tank) }
            ) {
                val icon = if (isInComparison) Icons.Filled.Clear else Icons.Filled.Add
                val tint = if (isInComparison) Color.Red else Color.Black

                Icon(
                    imageVector = icon,
                    contentDescription = "Compare",
                    tint = tint
                )
            }
        } else {
            Spacer(Modifier.width(50.dp))
        }

        IconToggleButton(
            checked = isFavourite,
            onCheckedChange = { onFavouriteClick(tank) }
        ) {
            val tint = if (isFavourite) Color.Yellow else Color.Black

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Favourite",
                tint = tint
            )
        }
    }
}
