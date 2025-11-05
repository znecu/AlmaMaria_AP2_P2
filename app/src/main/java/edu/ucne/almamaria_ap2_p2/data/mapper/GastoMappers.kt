package edu.ucne.almamaria_ap2_p2.data.mapper

import edu.ucne.almamaria_ap2_p2.data.remote.dto.GastoDto
import edu.ucne.almamaria_ap2_p2.domain.model.Gasto

fun GastoDto.toDomain(): Gasto = Gasto(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto
)

fun Gasto.toDto(): GastoDto = GastoDto(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto
)