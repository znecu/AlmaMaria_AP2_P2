package edu.ucne.almamaria_ap2_p2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.almamaria_ap2_p2.data.remote.Resource
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto
import edu.ucne.almamaria_ap2_p2.domain.usecase.GetGastoUseCase
import edu.ucne.almamaria_ap2_p2.domain.usecase.GetGastosUseCase
import edu.ucne.almamaria_ap2_p2.domain.usecase.SaveGastoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val getGastosUseCase: GetGastosUseCase,
    private val getGastoUseCase: GetGastoUseCase,
    private val saveGastoUseCase: SaveGastoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GastoUiState(isLoading = true))
    val state: StateFlow<GastoUiState> = _state.asStateFlow()

    init {
        obtenerGastos()
    }
    fun obtenerGastos() {
        viewModelScope.launch {
            getGastosUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                error = null,
                                message = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                ListaGastos = result,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
    private fun obtenerGasto(id: Int) {
        viewModelScope.launch {
            getGastoUseCase(id).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val gasto = result.data?.firstOrNull()
                        gasto?.let {
                            _state.update {
                                it.copy(
                                    gastoId = gasto.gastoId ?: 0,
                                    gastoFecha = gasto.fecha,
                                    gastoSuplidor = gasto.suplidor,
                                    gastoNcf = gasto.ncf,
                                    gastoItbis = gasto.itbis.toString(),
                                    gastoMonto = gasto.monto.toString(),
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(error = result.message, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: GastoUiEvent) {
        when (event) {
            is GastoUiEvent.CreateGasto -> crearGasto(event.gasto)
            is GastoUiEvent.GetGasto -> obtenerGasto(event.id)
            is GastoUiEvent.Load -> obtenerGastos()

            is GastoUiEvent.ShowBottonSheet -> {
                _state.update { it.copy(isLoading = false) }
            }

            is GastoUiEvent.HideBottonSheet -> {
                _state.update { it.copy(isLoading = false) }
            }

            is GastoUiEvent.OnFechaChange -> {
                _state.update { it.copy(gastoFecha = event.value) }
            }

            is GastoUiEvent.OnSuplidorChange -> {
                _state.update { it.copy(gastoSuplidor = event.value) }
            }

            is GastoUiEvent.OnNcfChange -> {
                _state.update { it.copy(gastoNcf = event.value) }
            }

            is GastoUiEvent.OnItbisChange -> {
                _state.update { it.copy(gastoItbis = event.value) }
            }

            is GastoUiEvent.OnMontoChange -> {
                _state.update { it.copy(gastoMonto = event.value) }
            }
        }
    }
    private fun crearGasto(gasto: Gasto) {
        viewModelScope.launch {
            when (val result = saveGastoUseCase(gasto)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            message = "Gasto creado correctamente",
                            gastoFecha = "",
                            gastoSuplidor = "",
                            gastoNcf = "",
                            gastoItbis = "",
                            gastoMonto = "",
                            error = null
                        )
                    }
                    obtenerGastos()
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(error = result.message)
                    }
                }

                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
