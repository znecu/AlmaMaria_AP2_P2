package edu.ucne.almamaria_ap2_p2.presentation

import edu.ucne.almamaria_ap2_p2.domain.model.Gasto

data class GastoUiState(
    val gasto: List<Gasto> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
