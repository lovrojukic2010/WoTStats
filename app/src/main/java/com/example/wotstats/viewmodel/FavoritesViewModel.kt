package com.example.wotstats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wotstats.api.client.WotClient
import com.example.wotstats.api.data.Vehicle
import com.example.wotstats.api.service.WotService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val favouritesCollection = "wot-stats"
    private val favouritesField = "favourites"

    private val service: WotService = WotService(WotClient.WotClientProvider.client)

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        val user = auth.currentUser ?: return

        val docRef = db.collection(favouritesCollection).document(user.uid)

        docRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val list = snapshot.get(favouritesField) as? List<Long> ?: emptyList()
                    val ids = list.map { it.toInt() }

                    _uiState.update {
                        it.copy(
                            favouriteIds = ids.toSet()
                        )
                    }

                    if (ids.isNotEmpty()) {
                        loadAllFavouriteVehicles(ids)
                    } else {
                        _uiState.update { it.copy(tanks = emptyList(), isLoading = false) }
                    }
                } else {
                    val data = mapOf(favouritesField to emptyList<Int>())
                    docRef.set(data)
                    _uiState.update { it.copy(tanks = emptyList(), isLoading = false) }
                }
            }
    }

    private fun loadAllFavouriteVehicles(ids: List<Int>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val vehicles = service.loadVehicles(
                    limit = ids.size,
                    pageNo = 1,
                    tankId = ids,
                    fields = "tank_id,name,short_name,nation,tier,images.big_icon"
                )

                val sortedVehicles = vehicles.sortedBy { it.tankId }

                _uiState.update {
                    it.copy(
                        tanks = sortedVehicles,
                        isLoading = false
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onRemoveFavouriteClicked(tank: Vehicle) {
        val user = auth.currentUser ?: return
        val id = tank.tankId

        val docRef = db.collection(favouritesCollection).document(user.uid)

        _uiState.update { state ->
            val newFavouriteIds = state.favouriteIds - id
            val newTanks = state.tanks.filter { it.tankId != id }

            val newList = newFavouriteIds.toList()
            docRef.set(mapOf(favouritesField to newList))

            state.copy(
                favouriteIds = newFavouriteIds,
                tanks = newTanks
            )
        }
    }
}

data class FavoritesUiState(
    val tanks: List<Vehicle> = emptyList(),
    val isLoading: Boolean = false,
    val favouriteIds: Set<Int> = emptySet()
)
