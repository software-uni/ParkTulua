package com.univalle.parqueaderoapp.data.vehiculos

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.univalle.parqueaderoapp.data.supabase.SupabaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject

class SupabaseVehiculoRepository(
    private val auth: FirebaseAuth,
    private val cliente: OkHttpClient,
    private val gson: Gson
) : VehiculoRepository {

    override suspend fun registraVehiculos(tipoCodigo: String, placa: String?): Result<Unit> {
        val cuerpo = JSONObject()
            .put("p_tipo_codigo", tipoCodigo)
            .put("p_placa", placa)
            .toString()
        return llamarRpc(SupabaseConfig.URL_REGISTRAR_VEHICULO, cuerpo).map { }
    }

    override suspend fun obtenerVehiculosUsuario(): Result<List<VehiculoEntity>> =  withContext(Dispatchers.IO) {
            Log.d("Supabase", "1. Función llamada")
            val usuario = auth.currentUser
                ?: return@withContext Result.failure(IllegalStateException("No hay sesion activa"))

            try {
                val resultadoToken = usuario.getIdToken(false).await()
                val token = resultadoToken.token
                Log.d("Supabase", "role en el token = ${resultadoToken.claims["role"]}")
                val url = "${SupabaseConfig.REST_URL}/usuario_vehiculo" +
                        "?select=vehiculo(id,placa,tipo_vehiculo(nombre)),usuario!inner(firebase_uid)" +
                        "&activo=eq.true" +
                        "&usuario.firebase_uid=eq.${usuario.uid}"

                val peticion = Request.Builder()
                    .url(url)
                    .header("apikey", SupabaseConfig.ANON_KEY)
                    .header("Authorization", "Bearer $token")
                    .get()
                    .build()

                cliente.newCall(peticion).execute().use { respuesta ->
                    val cuerpo = respuesta.body?.string() ?: "[]"
                    Log.d("Supabase", "code=${respuesta.code} body=$cuerpo")

                    if (!respuesta.isSuccessful) {
                        return@withContext Result.failure(IOException("Error ${respuesta.code}: $cuerpo"))
                    }
                    val tipoLista = object : TypeToken<List<UsuarioVehiculoDto>>() {}.type
                    val filas: List<UsuarioVehiculoDto> = gson.fromJson(cuerpo, tipoLista)

                    val vehiculos = filas.mapNotNull { fila ->
                        val v = fila.vehiculo ?: return@mapNotNull null
                        val id = v.id ?: return@mapNotNull null
                        VehiculoEntity(
                            id = id,
                            placa = v.placa ?.takeIf { it.isNotBlank() },
                            tipo = v.tipoVehiculo?.nombre ?: "Sin tipo"
                        )
                    }
                    Result.success(vehiculos)
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
    }

    override suspend fun suspenderVehiculo(vehiculoId: String): Result<Unit> {
        val cuerpo = JSONObject().put("p_vehiculo_id", vehiculoId).toString()
        return llamarRpc(SupabaseConfig.URL_QUITAR_VEHICULO, cuerpo).map { }
    }

    private suspend fun llamarRpc(url:String, cuerpoJson: String): Result<String> = withContext(Dispatchers.IO){
        // 1 ¿hay session?
        val usuario = auth.currentUser
            ?: return@withContext Result.failure(IllegalStateException("No hay sesion activa"))
        try {
            // 2 token de firebase
            val token = usuario.getIdToken(false).await().token

            //3 cuerpo
            val cuerpo = cuerpoJson.toRequestBody("application/json".toMediaType())

            //4 peticion
            val peticion = Request.Builder()
                .url(url)
                .header("apikey", SupabaseConfig.ANON_KEY)
                .header("Authorization", "Bearer $token")
                .post(cuerpo)
                .build()
            cliente.newCall(peticion).execute().use { respuesta ->
                val texto = respuesta.body?.string().orEmpty()
                if (!respuesta.isSuccessful) {
                    val mensaje = if (texto.isNotBlank()) {
                        try {
                            JSONObject(texto).optString("message", "Error ${respuesta.code}")
                        } catch (e: Exception) {
                            "Error ${respuesta.code}: $texto"
                        }
                    }
                    else {
                        "Error ${respuesta.code}: El servidor no devolvió detalles."
                    }
                    return@withContext Result.failure(IOException(mensaje))
                }
                Result.success(texto)
            }
        }
        catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
