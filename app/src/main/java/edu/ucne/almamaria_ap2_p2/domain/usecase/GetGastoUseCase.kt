package edu.ucne.almamaria_ap2_p2.domain.usecase

import edu.ucne.almamaria_ap2_p2.data.remote.Resource
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto
import edu.ucne.almamaria_ap2_p2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGastoUseCase @Inject constructor(
    private val repository: GastoRepository
) {
    operator fun invoke (id: Int): Flow<Resource<List<Gasto>>> {
        return repository.getGasto(id)
    }
}