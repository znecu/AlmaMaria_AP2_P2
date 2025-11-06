package edu.ucne.almamaria_ap2_p2.data.remote.repository

import androidx.compose.ui.geometry.Rect
import coil.network.HttpException
import edu.ucne.almamaria_ap2_p2.data.mapper.toDomain
import edu.ucne.almamaria_ap2_p2.data.mapper.toDto
import edu.ucne.almamaria_ap2_p2.data.remote.RemoteDataSource
import edu.ucne.almamaria_ap2_p2.data.remote.Resource
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto
import edu.ucne.almamaria_ap2_p2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class GastoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : GastoRepository {

    override fun getGasto(id: Int): Flow<Resource<List<Gasto>>> = flow {
        try {
            emit(Resource.Loading<List<Gasto>>())
            val gastoDto = remoteDataSource.getGasto(id)
            val gasto = gastoDto.map { it.toDomain() }
            emit(Resource.Success(gasto))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de servidor: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.localizedMessage}"))
        }
    }

    override suspend fun saveGasto(gasto: Gasto): Resource<Unit> {
        return try {
            val dto = gasto.toDto()
            remoteDataSource.saveGasto(dto)
            Resource.Success(Unit)
        } catch (e: HttpException){
            Resource.Error("Error de seervidor: ${e.message}")
        } catch (e: Exception){
            Resource.Error("Error desconocido: ${e.localizedMessage}")
        }
    }

    override fun getGastos(): Flow<Resource<List<Gasto>>> = flow {
        try {
            emit(Resource.Loading())
            val gastosDto = remoteDataSource.getGastos()
            val gastos = gastosDto.map { it.toDomain() }
            emit(Resource.Success(gastos))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de servidor: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.localizedMessage}"))
        }
    }
}