package edu.ucne.almamaria_ap2_p2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto
import edu.ucne.almamaria_ap2_p2.domain.usecase.GetGastoUseCase
import edu.ucne.almamaria_ap2_p2.domain.usecase.SaveGastoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val getGastosUseCase: GetGastoUseCase,
    private val saveGastoUseCase: SaveGastoUseCase,

) : ViewModel() {
    private val _state = MutableStateFlow(GastoUiState(isLoading = true))
    val state: StateFlow<GastoUiState> = _state.asStateFlow()

    init {
        obtenerGastos()
    }

    fun obtenerGastos() {
        viewModelScope.launch {
            getGastosUseCase().collect { gastos ->
                _state.value = _state.value.copy(
                    listaGastos = gastos,
                    isLoading = false
                )
            }
        }
    }

    suspend fun onEvent(event: GastoUiEvent) {
        when (event) {
            is GastoUiEvent.CreateGasto -> CrearGasto()

        }
    }

    private fun CrearGasto(gasto: Gasto) {
        viewModelScope.launch {

            when (val result = saveGastoUseCase()) {
                is Gasto -> {
                    _state.update {
                        it.copy(
                            userMessage = "Gasto creado",
                            gastoFecha = "",
                            gastoSuplidor = "",
                            gastoNcf = "",
                            gastoItbis = "",
                            gastoMonto = "",
                            showBottomSheet = false
                        )
                    }
                    obtenerGastos()
                }
                else -> {}
            }
        }
    }
}
