package com.example.wotstats.view.components.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

            TankRow(tank = tank)
        }
    }
}

@Composable
fun TankRow(tank: Vehicle) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = tank.images?.bigIcon,
            contentDescription = tank.name,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = tank.tier?.toRomanTier().orEmpty(),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = tank.shortName ?: tank.name.orEmpty(),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )

        Spacer(Modifier.width(8.dp))
    }
}