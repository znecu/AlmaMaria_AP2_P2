package edu.ucne.almamaria_ap2_p2.presentation

import edu.ucne.almamaria_ap2_p2.domain.model.Gasto

interface GastoUiEvent {
    data object Load : GastoUiEvent
    data class CrearGasto(val gasto: Gasto) : GastoUiEvent
    data class GetGasto(val id: Int) : GastoUiEvent

    object ShowBottonSheet : GastoUiEvent
    object HideBottonSheet : GastoUiEvent

    data class OnFechaChange(val value: String): GastoUiEvent
    data class OnSuplidorChange(val value: String): GastoUiEvent
    data class OnNcfChange(val value: String): GastoUiEvent
    data class OnItbisChange(val value: String): GastoUiEvent
    data class OnMontoChange(val value: String): GastoUiEvent

}