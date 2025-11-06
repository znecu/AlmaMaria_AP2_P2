package edu.ucne.almamaria_ap2_p2.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.almamaria_ap2_p2.data.remote.Resource
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GastoScreen(
    viewModel: GastoViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GastoScreenBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoScreenBody(
    state: GastoUiState,
    onEvent: (GastoUiEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(GastoUiEvent.ShowBottonSheet) },
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Gasto")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val gastos = state.ListaGastos) {
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is Resource.Success -> {
                    val data = gastos.data ?: emptyList()
                    if (data.isEmpty()) {
                        Text(
                            text = "No hay gastos registrados",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(data, key = { it.gastoId ?: 0 }) { gasto ->
                                GastoItem(
                                    gasto = gasto,
                                    onEdit = {
                                        onEvent(GastoUiEvent.GetGasto(gasto.gastoId ?: 0))
                                        onEvent(GastoUiEvent.ShowBottonSheet)
                                    }
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = gastos.message ?: "Error al cargar los datos",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (state.gastoFecha.isNotBlank() || state.gastoSuplidor.isNotBlank() || state.gastoNcf.isNotBlank()
            || state.gastoItbis.isNotBlank() || state.gastoMonto.isNotBlank()
            || state.isLoading.not()
        ) {
            if (sheetState.isVisible || state.isLoading.not()) {
                ModalBottomSheet(
                    onDismissRequest = {
                        onEvent(GastoUiEvent.HideBottonSheet)
                        limpiarCampos(onEvent)
                    },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = if (state.gastoId > 0) "Editar Gasto" else "Nuevo Gasto",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        OutlinedTextField(
                            value = state.gastoFecha,
                            onValueChange = { onEvent(GastoUiEvent.OnFechaChange(it)) },
                            label = { Text("Fecha") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.gastoSuplidor,
                            onValueChange = { onEvent(GastoUiEvent.OnSuplidorChange(it)) },
                            label = { Text("Suplidor") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.gastoNcf,
                            onValueChange = { onEvent(GastoUiEvent.OnNcfChange(it)) },
                            label = { Text("NCF") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.gastoItbis,
                            onValueChange = { onEvent(GastoUiEvent.OnItbisChange(it)) },
                            label = { Text("ITBIS") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.gastoMonto,
                            onValueChange = { onEvent(GastoUiEvent.OnMontoChange(it)) },
                            label = { Text("Monto") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onEvent(GastoUiEvent.HideBottonSheet)
                                    limpiarCampos(onEvent)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }

                            Button(
                                onClick = {
                                    if (state.gastoFecha.isNotBlank() &&
                                        state.gastoSuplidor.isNotBlank() &&
                                        state.gastoNcf.isNotBlank() &&
                                        state.gastoItbis.isNotBlank() &&
                                        state.gastoMonto.isNotBlank()
                                    ) {
                                        val gasto = Gasto(
                                            gastoId = if (state.gastoId > 0) state.gastoId else null,
                                            fecha = state.gastoFecha,
                                            suplidor = state.gastoSuplidor,
                                            ncf = state.gastoNcf,
                                            itbis = state.gastoItbis.toDoubleOrNull() ?: 0.0,
                                            monto = state.gastoMonto.toDoubleOrNull() ?: 0.0
                                        )
                                        onEvent(GastoUiEvent.CrearGasto(gasto))
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = state.gastoFecha.isNotBlank() &&
                                        state.gastoSuplidor.isNotBlank() &&
                                        state.gastoNcf.isNotBlank() &&
                                        state.gastoItbis.isNotBlank() &&
                                        state.gastoMonto.isNotBlank()
                            ) {
                                Text("Guardar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: Gasto,
    onEdit: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Suplidor: ${gasto.suplidor}", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Fecha: ${gasto.fecha}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "NCF: ${gasto.ncf}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row {
                    Text(
                        text = "ITBIS: ${NumberFormat.getCurrencyInstance(Locale.US).format(gasto.itbis)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = " | Monto: ${NumberFormat.getCurrencyInstance(Locale.US).format(gasto.monto)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar gasto"
                )
            }
        }
    }
}

private fun limpiarCampos(onEvent: (GastoUiEvent) -> Unit) {
    onEvent(GastoUiEvent.OnFechaChange(""))
    onEvent(GastoUiEvent.OnSuplidorChange(""))
    onEvent(GastoUiEvent.OnNcfChange(""))
    onEvent(GastoUiEvent.OnItbisChange(""))
    onEvent(GastoUiEvent.OnMontoChange(""))
}
@Preview(showBackground = true)
@Composable
fun GastoScreenPreview() {
    val sampleState = GastoUiState(
        ListaGastos = Resource.Success(
            listOf(
                Gasto(1, "2025-11-05", "Supermercado Bravo", "A010089561", 23.0, 230.0),
                Gasto(2, "2025-11-06", "Ferretería La Grande", "B123456789", 35.0, 500.0)
            )
        )
    )
    MaterialTheme {
        GastoScreenBody(sampleState) {}
    }
}