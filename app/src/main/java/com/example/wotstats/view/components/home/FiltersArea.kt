package com.example.wotstats.view.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wotstats.R

@Composable
fun FiltersArea(
    selectedTier: Int?,
    selectedNation: String?,
    onTierSelected: (Int?) -> Unit,
    onNationSelected: (String?) -> Unit
) {
    val tiers = listOf("All", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI")
    val nations = listOf(
        "All",
        "Germany",
        "USA",
        "France",
        "UK",
        "USSR",
        "Poland",
        "Sweden",
        "Italy",
        "Czech",
        "Japan",
        "China"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            tiers.forEachIndexed { index, tier ->
                val tierValue: Int? = if (index == 0) null else index
                TierFilterButton(
                    text = tier,
                    selected = tierValue == selectedTier,
                    onClick = { onTierSelected(tierValue) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            nations.forEachIndexed { index, nation ->
                val nationValue: String? =
                    if (index == 0) null else nation.lowercase()

                if (index == 0) {
                    NationAllButton(
                        selected = selectedNation == null,
                        onClick = { onNationSelected(null) }
                    )
                } else {
                    val resId = when (nation) {
                        "Germany" -> R.drawable.flag_germany
                        "USA" -> R.drawable.flag_usa
                        "France" -> R.drawable.flag_france
                        "UK" -> R.drawable.flag_england
                        "USSR" -> R.drawable.flag_ussr
                        "Poland" -> R.drawable.flag_poland
                        "Sweden" -> R.drawable.flag_sweden
                        "Italy" -> R.drawable.flag_italy
                        "Czech" -> R.drawable.flag_czech
                        "Japan" -> R.drawable.flag_japan
                        "China" -> R.drawable.flag_china
                        else -> R.drawable.flag_germany
                    }
                    NationFilterButton(
                        flagRes = resId,
                        selected = nationValue == selectedNation,
                        onClick = { onNationSelected(nationValue) }
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
