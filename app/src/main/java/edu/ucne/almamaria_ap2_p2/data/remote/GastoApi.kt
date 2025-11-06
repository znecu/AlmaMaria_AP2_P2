package edu.ucne.almamaria_ap2_p2.data.remote

import edu.ucne.almamaria_ap2_p2.data.remote.dto.GastoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GastoApi {

    @GET("api/Gastos")
    suspend fun getGastos(): List<GastoDto>
    @GET("api/Gastos/{gastoId}")
    suspend fun getGasto(@Path("gastoId") id: Int): List<GastoDto>

    @POST("api/Gastos")
    suspend fun saveGasto(@Body gastoDto: GastoDto)

}