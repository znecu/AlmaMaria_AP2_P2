package edu.ucne.almamaria_ap2_p2.presentation

import edu.ucne.almamaria_ap2_p2.data.remote.Resource
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto

data class GastoUiState(
    val ListaGastos: Resource<List<Gasto>> = Resource.Loading(),
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,

    val gastoId: Int = 0,
    val gastoFecha: String = "",
    val gastoSuplidor: String = "",
    val gastoNcf: String = "",
    val gastoItbis: String = "",
    val gastoMonto: String = "",
)
