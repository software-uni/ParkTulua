package com.univalle.parqueaderoapp.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.univalle.parqueaderoapp.data.auth.AuthRepository
import com.univalle.parqueaderoapp.data.model.ParqueaderoMapaDto
import com.univalle.parqueaderoapp.data.model.toDomainOrNull
import com.univalle.parqueaderoapp.data.parqueaderos.ParqueaderoRepository
import com.univalle.parqueaderoapp.data.supabase.SupabaseConfig
import com.univalle.parqueaderoapp.domain.model.ParqueaderoMapa
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class SupabaseParqueaderoRepository(
    private val authRepository: AuthRepository,
    private val cliente: OkHttpClient,
    private val gson: Gson
) : ParqueaderoRepository {

    override suspend fun obtenerParqueaderos(): Result<List<ParqueaderoMapa>> =
        withContext(Dispatchers.IO) {
            try {
                val token = authRepository.obtenerToken()
                    ?: return@withContext Result.failure(
                        IllegalStateException("No hay sesion activa")
                    )

                val peticion = Request.Builder()
                    .url(SupabaseConfig.URL_PARQUEADEROS_MAPA)
                    .header("apikey", SupabaseConfig.ANON_KEY)
                    .header("Authorization", "Bearer $token")
                    .post("{}".toRequestBody("application/json".toMediaType()))
                    .build()

                cliente.newCall(peticion).execute().use { respuesta ->
                    val texto = respuesta.body?.string().orEmpty()
                    if (!respuesta.isSuccessful) {
                        return@withContext Result.failure(IOException("Error ${respuesta.code}"))
                    }
                    val tipoLista = object : TypeToken<List<ParqueaderoMapaDto>>() {}.type
                    val filas: List<ParqueaderoMapaDto>? = gson.fromJson(texto, tipoLista)
                    Result.success(filas.orEmpty().mapNotNull { it.toDomainOrNull() })
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}