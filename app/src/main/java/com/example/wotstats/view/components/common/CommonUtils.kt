package com.example.wotstats.view.components.common

import com.example.wotstats.api.data.AmmoDetails

class CommonUtils {
    companion object {
        fun resolveAmmo(ammoDetails: List<AmmoDetails>?): AmmoDetails? {
            if (ammoDetails == null) {
                return null
            }

            if (ammoDetails.size == 1) {
                return ammoDetails[0]
            }
            var goldGrenade = AmmoDetails(listOf(0, 0, 0), listOf(0, 0, 0))
            var normalGrenade = AmmoDetails(listOf(0, 0, 0), listOf(0, 0, 0))
            for (detail in ammoDetails) {
                if (detail.penetration[1] >= goldGrenade.penetration[1]) {
                    normalGrenade = goldGrenade
                    goldGrenade = detail
                } else if (detail.penetration[1] >= normalGrenade.penetration[1]) {
                    normalGrenade = detail
                }
            }
            return normalGrenade
        }
    }
}