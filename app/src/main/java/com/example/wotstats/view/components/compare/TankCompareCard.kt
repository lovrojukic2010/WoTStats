package com.example.wotstats.view.components.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wotstats.api.data.Vehicle
import com.example.wotstats.view.components.common.CommonUtils.Companion.resolveAmmo

@Composable
fun TankCompareCard(
    leftTank: Vehicle?,
    rightTank: Vehicle?,
    modifier: Modifier = Modifier
) {
    val leftAmmo = resolveAmmo(leftTank?.details?.ammo)
    val rightAmmo = resolveAmmo(rightTank?.details?.ammo)

    val leftDamage = (leftAmmo?.damage?.getOrNull(1) ?: 0).toFloat()
    val rightDamage = (rightAmmo?.damage?.getOrNull(1) ?: 0).toFloat()

    val leftPen = (leftAmmo?.penetration?.getOrNull(1) ?: 0).toFloat()
    val rightPen = (rightAmmo?.penetration?.getOrNull(1) ?: 0).toFloat()

    val leftReload = leftTank?.details?.gun?.reloadTime ?: 0f
    val rightReload = rightTank?.details?.gun?.reloadTime ?: 0f

    val leftSpeed = (leftTank?.details?.speedForward ?: 0).toFloat()
    val rightSpeed = (rightTank?.details?.speedForward ?: 0).toFloat()

    val leftHp = (leftTank?.details?.hp ?: 0).toFloat()
    val rightHp = (rightTank?.details?.hp ?: 0).toFloat()

    val leftCaliber = (leftTank?.details?.gun?.caliber ?: 0).toFloat()
    val rightCaliber = (rightTank?.details?.gun?.caliber ?: 0).toFloat()

    val leftWeight = (leftTank?.details?.weight ?: 0).toFloat()
    val rightWeight = (rightTank?.details?.weight ?: 0).toFloat()

    val leftPower = (leftTank?.details?.engine?.power ?: 0).toFloat()
    val rightPower = (rightTank?.details?.engine?.power ?: 0).toFloat()

    val leftDisp = (leftTank?.details?.gun?.dispersion ?: 0.0).toFloat()
    val rightDisp = (rightTank?.details?.gun?.dispersion ?: 0.0).toFloat()

    val leftFireRate = leftTank?.details?.gun?.fireRate ?: 0.0f
    val rightFireRate = rightTank?.details?.gun?.fireRate ?: 0.0f

    fun dpm(dmg: Float, fireRate: Float): Float =
        if (dmg == 0f) 0f else kotlin.math.floor(fireRate) * dmg

    val leftDpm = dpm(leftDamage, leftFireRate)
    val rightDpm = dpm(rightDamage, rightFireRate)


    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = leftTank?.images?.bigIcon,
                    contentDescription = leftTank?.shortName,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(0.8f),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = leftTank?.shortName.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = rightTank?.images?.bigIcon,
                    contentDescription = rightTank?.shortName,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(0.8f),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = rightTank?.shortName.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCompareRow("Damage", leftDamage, rightDamage, moreIsBetter = true)
            StatCompareRow("Reload (sec)", leftReload, rightReload, moreIsBetter = false)
            StatCompareRow("Top speed (km/h)", leftSpeed, rightSpeed, moreIsBetter = true)
            StatCompareRow("Health", leftHp, rightHp, moreIsBetter = true)
            StatCompareRow("Caliber (mm)", leftCaliber, rightCaliber, moreIsBetter = true)
            StatCompareRow("Penetration (mm)", leftPen, rightPen, moreIsBetter = true)
            StatCompareRow("Weight (kg)", leftWeight, rightWeight, moreIsBetter = false)
            StatCompareRow("Power (hp)", leftPower, rightPower, moreIsBetter = true)
            StatCompareRow("DPM", leftDpm, rightDpm, moreIsBetter = true)
            StatCompareRow("Dispersion (m/100m)", leftDisp, rightDisp, moreIsBetter = false)
        }
    }
}

@Composable
private fun StatCompareRow(
    label: String,
    left: Float,
    right: Float,
    moreIsBetter: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, color = Color.Black, modifier = Modifier.width(180.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Text(
                text = left.toString().removeSuffix(".0"),
                textAlign = TextAlign.Right,
                color = statColor(left, right, moreIsBetter),
                modifier = Modifier.width(60.dp)

            )
            Text(
                text = right.toString().removeSuffix(".0"),
                color = statColor(right, left, moreIsBetter),
                textAlign = TextAlign.Right,
                modifier = Modifier.width(70.dp)
            )
        }
    }
}

private fun statColor(
    valueThis: Float,
    valueOther: Float,
    moreIsBetter: Boolean
): Color {
    if (valueThis == valueOther) return Color.Black

    val isBetter = if (moreIsBetter) {
        valueThis > valueOther
    } else {
        valueThis < valueOther
    }

    return if (isBetter) Color(0xFF2E7D32) else Color(0xFFC62828)
}