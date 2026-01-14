package com.example.wotstats.view.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wotstats.api.data.Vehicle
import com.example.wotstats.view.components.common.CommonUtils.Companion.resolveAmmo
import kotlin.math.floor

@Composable
fun TankStatsCard(
    tank: Vehicle?,
    modifier: Modifier = Modifier
) {

    val ammoDetails = resolveAmmo(tank?.details?.ammo)
    val damage = ammoDetails?.damage[1] ?: 0
    val penetration = ammoDetails?.penetration[1] ?: 0
    val reload = tank?.details?.gun?.reloadTime ?: 0.0f
    val topSpeed = tank?.details?.speedForward ?: 0
    val hp = tank?.details?.hp ?: 0
    val weight = tank?.details?.weight ?: 0
    val power = tank?.details?.engine?.power ?: 0
    val dispersion = tank?.details?.gun?.dispersion ?: 0.0
    val caliber = tank?.details?.gun?.caliber ?: 0
    val rateOfFire = tank?.details?.gun?.fireRate ?: 0.0f
    var dpm = 0
    if (damage != 0) {
        dpm = floor(rateOfFire).toInt() * damage
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = tank?.images?.bigIcon,
                contentDescription = tank?.name,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.8f)
                    .height(160.dp),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = tank?.name.orEmpty(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatRow(label = "Damage", value = damage.toString())
                StatRow(label = "Reload (sec)", value = reload.toString())
                StatRow(label = "Top speed (km/h)", value = topSpeed.toString())
                StatRow(label = "Health", value = hp.toString())
                StatRow(label = "Caliber (mm)", value = caliber.toString())
                StatRow(label = "Penetration (mm)", value = penetration.toString())
                StatRow(label = "Weight (kg)", value = weight.toString())
                StatRow(label = "Power (hp)", value = power.toString())
                StatRow(label = "DPM", value = dpm.toString())
                StatRow(label = "Dispersion (m/100m)", value = dispersion.toString())
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Black)
        Text(text = value, color = Color.Black)
    }
}