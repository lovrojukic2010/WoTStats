package com.example.wotstats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wotstats.api.client.WotClient
import com.example.wotstats.api.data.Vehicle
import com.example.wotstats.api.service.WotService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehiclesViewModel(
) : ViewModel() {

    private val service: WotService = WotService(WotClient.WotService.client)
    private val _uiState = MutableStateFlow(VehiclesUiState())
    val uiState: StateFlow<VehiclesUiState> = _uiState

    private val pageSize = 10

    init {
        loadNextPage()
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
}

data class VehiclesUiState(
    val tanks: List<Vehicle> = emptyList(),
    val pageNo: Int = 1,
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val selectedTier: Int? = null,
    val selectedNation: String? = null
)