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

class VehiclesViewModel(
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val favouritesCollection = "wot-stats"

    private val favouritesField = "favourites"
    private val service: WotService = WotService(WotClient.WotService.client)
    private val _uiState = MutableStateFlow(VehiclesUiState())
    val uiState: StateFlow<VehiclesUiState> = _uiState

    private val pageSize = 10

    init {
        loadFavourites()
        loadNextPage()
    }

    private fun loadFavourites() {
        val user = auth.currentUser ?: return

        val docRef = db.collection(favouritesCollection).document(user.uid)

        docRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val list = snapshot.get(favouritesField) as? List<Long> ?: emptyList()
                    val ids = list.map { it.toInt() }.toSet()
                    _uiState.update { it.copy(favouriteIds = ids) }
                } else {
                    val data = mapOf(favouritesField to emptyList<Int>())
                    docRef.set(data)
                }
            }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.endReached) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val newTanks = service.loadVehicles(
                    limit = pageSize,
                    pageNo = state.pageNo,
                    tier = state.selectedTier,
                    nation = state.selectedNation
                )

                _uiState.update {
                    it.copy(
                        tanks = it.tanks + newTanks,
                        pageNo = it.pageNo + 1,
                        isLoading = false,
                        endReached = newTanks.isEmpty()
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setFilters(tier: Int?, nation: String?) {
        val current = _uiState.value
        if (current.selectedTier == tier && current.selectedNation == nation) {
            return
        }
        _uiState.update {
            it.copy(
                tanks = emptyList(),
                pageNo = 1,
                endReached = false,
                isLoading = false,
                selectedTier = tier,
                selectedNation = nation
            )
        }
        loadNextPage()
    }

    fun onCompareClicked(tank: Vehicle) {
        _uiState.update { state ->
            val id = tank.tankId

            val isAlreadyIn = state.comparisonIds.contains(id)

            val newComparisonIds =
                if (isAlreadyIn) {
                    state.comparisonIds - id
                } else {
                    if (state.comparisonIds.size < 2) {
                        state.comparisonIds + id
                    } else {
                        state.comparisonIds
                    }
                }

            state.copy(comparisonIds = newComparisonIds)
        }
    }

    fun onFavouriteClicked(tank: Vehicle) {
        val user = auth.currentUser ?: return
        val id = tank.tankId

        val docRef = db.collection(favouritesCollection).document(user.uid)

        _uiState.update { state ->
            val newFavouriteIds =
                if (state.favouriteIds.contains(id)) {
                    state.favouriteIds - id
                } else {
                    state.favouriteIds + id
                }

            val newList = newFavouriteIds.toList()

            docRef.set(mapOf(favouritesField to newList))

            state.copy(favouriteIds = newFavouriteIds)
        }
    }

}

data class VehiclesUiState(
    val tanks: List<Vehicle> = emptyList(),
    val pageNo: Int = 1,
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val selectedTier: Int? = null,
    val selectedNation: String? = null,
    val comparisonIds: Set<Int> = emptySet(),
    val favouriteIds: Set<Int> = emptySet()
)
