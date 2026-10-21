package com.univalle.parqueaderoapp.data.vehiculo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
interface VehiculoRepository {
    suspend fun obtenerVehiculosDelUsuario(): Result<List<Vehiculo>>
}
class SupabaseVehiculoRepository(
    private val supabase : SupabaseClient
) : VehiculoRepository {
    override suspend fun obtenerVehiculosDelUsuario(): Result<List<Vehiculo>> {

        return try {
            val vehiculos = supabase.postgrest["vehiculo"]
                .select()
                .decodeList<Vehiculo>()
            Result.success(vehiculos)

        }catch (e : Exception){
            Result.failure(Exception("Error al cargar los vehículos: ${e.message}"))
        }
    }
}