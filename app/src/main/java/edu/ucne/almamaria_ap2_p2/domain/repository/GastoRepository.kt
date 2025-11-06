package edu.ucne.almamaria_ap2_p2.domain.repository

import edu.ucne.almamaria_ap2_p2.data.remote.Resource
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GastoRepository {
    fun getGastos(): Flow<Resource<List<Gasto>>>
    fun getGasto(id: Int): Flow<Resource<List<Gasto>>>

    suspend fun saveGasto(gasto: Gasto): Resource<Unit>

}