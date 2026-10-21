package com.univalle.parqueaderoapp.data.reserva

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseReservaRepository(
    private val supabase: SupabaseClient
) : ReservaRepository {

    override suspend fun crearReserva(
        parqueaderoId: String,
        vehiculoId: String,
        duracionEstimadaMin: Int,
        minutosParaLlegar: Int
    ): Result<ReservaResponse> {
        return try {
            // 1. Empacamos los datos con los nombres exactos que pusiste en tu función de PostgreSQL
            val parametros = buildJsonObject {
                put("p_parqueadero_id", parqueaderoId)
                put("p_vehiculo_id", vehiculoId)
                put("p_duracion_estimada_min", duracionEstimadaMin)
                put("p_minutos_para_llegar", minutosParaLlegar)
            }

            // 2. Disparamos la función RPC en Supabase y guardamos la respuesta en el modelo que creaste
            val respuesta = supabase.postgrest.rpc("crear_reserva", parametros).decodeAs<ReservaResponse>()

            // 3. Avisamos que todo salió bien
            Result.success(respuesta)

        } catch (e: Exception) {
            // 4. Si la base de datos rechaza la reserva (ej. no hay cupo), capturamos el error
            Result.failure(Exception(e.message))
        }
    }
}