package edu.ucne.almamaria_ap2_p2.domain.usecase

import edu.ucne.almamaria_ap2_p2.domain.model.Gasto
import edu.ucne.almamaria_ap2_p2.domain.repository.GastoRepository
import javax.inject.Inject

class SaveGastoUseCase @Inject constructor(
    private val repository: GastoRepository
) {
    suspend operator fun invoke(gasto: Gasto) = repository.saveGasto(gasto)
}