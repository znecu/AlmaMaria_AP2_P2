package edu.ucne.almamaria_ap2_p2.presentation

interface GastoUiEvent {
    data object Load : GastoUiEvent
    data class CreateGasto(val fecha: String, val suplidor: String, val ncf: String,
                         val itbis: Double, val monto: Double): GastoUiEvent

    object ShowBottonSheet : GastoUiEvent
    object HideBottonSheet : GastoUiEvent

    data class OnFechaChange(val value: String): GastoUiEvent
    data class OnSuplidorChange(val value: String): GastoUiEvent
    data class OnNcfChange(val value: String): GastoUiEvent
    data class OnItbisChange(val value: String): GastoUiEvent
    data class OnMontoChange(val value: String): GastoUiEvent

}