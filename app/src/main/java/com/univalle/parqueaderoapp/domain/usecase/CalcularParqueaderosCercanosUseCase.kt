package com.univalle.parqueaderoapp.domain.usecase

import com.univalle.parqueaderoapp.core.util.GeoUtils
import com.univalle.parqueaderoapp.data.ubicacion.Coordenada
import com.univalle.parqueaderoapp.domain.model.FiltroMapa
import com.univalle.parqueaderoapp.domain.model.ParqueaderoCercano
import com.univalle.parqueaderoapp.domain.model.ParqueaderoMapa
import kotlin.math.roundToInt

class CalcularParqueaderosCercanosUseCase  {

    operator fun invoke(
        parqueaderos: List<ParqueaderoMapa>,
        origen: Coordenada,
        filtro: FiltroMapa
    ): List<ParqueaderoCercano> {
        return parqueaderos.asSequence()
            .map { parqueadero ->
                val distancia = GeoUtils.calcularDistanciaMetros(
                    origen = origen,
                    latitudDestino = parqueadero.latitud,
                    longitudDestino = parqueadero.longitud
                )
                parqueadero to distancia
            }
            .filter { (_, distancia) -> distancia <= filtro.radioMetros }
            .map { (parqueadero, distancia) ->
                val tiposVisibles = parqueadero.tipos.filter { tipo ->
                    filtro.tipoCodigo == null || tipo.tipoCodigo == filtro.tipoCodigo
                }
                ParqueaderoCercano(parqueadero, distancia.roundToInt(), tiposVisibles)
            }
            .filter { cercano ->
                cercano.tiposVisibles.isNotEmpty() &&
                        (!filtro.soloDisponibles || cercano.tiposVisibles.any { it.libres > 0})
            }
            .sortedBy { it.distanciaMetros }
            .take(filtro.maxResultados)
            .toList()
    }
}