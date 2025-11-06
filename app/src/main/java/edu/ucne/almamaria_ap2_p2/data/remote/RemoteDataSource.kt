package edu.ucne.almamaria_ap2_p2.data.remote

import edu.ucne.almamaria_ap2_p2.data.remote.GastoApi
import edu.ucne.almamaria_ap2_p2.data.remote.dto.GastoDto
import javax.inject.Inject

data class RemoteDataSource @Inject constructor(
    private val gastoApi: GastoApi
){
    suspend fun getGastos(): List<GastoDto> = gastoApi.getGastos()
    suspend fun getGasto(id: Int): List<GastoDto> = gastoApi.getGasto(id)

    suspend fun saveGasto(gastoDto: GastoDto) = gastoApi.saveGasto(gastoDto)
}