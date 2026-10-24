package com.univalle.parqueaderoapp.data.usuario

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.univalle.parqueaderoapp.data.supabase.SupabaseConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

interface UsuarioRepository {
    suspend fun sincronizarUsuario(foto: ByteArray? = null): Result<Unit>
    suspend fun actualizarPerfil(datos: UsuarioUpdateDto): Result<Unit>
    suspend fun actualizarFotoPerfil(foto: ByteArray): Result<String>
    suspend fun obtenerRutaFotoPerfil(): Result<String?>
    suspend fun eliminarCuenta(): Result<Unit>
    fun urlFotoPerfil(ruta: String): String

}

class SupabaseUsuarioRepository(
    private val auth: FirebaseAuth,
    private val cliente: OkHttpClient,
    private val gson: Gson,

    ) : UsuarioRepository {

    override suspend fun sincronizarUsuario(foto: ByteArray?): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val token = auth.currentUser?.getIdToken(false)?.await()?.token
                    ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))

                val cuerpo = if (foto != null) {
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "foto",
                            "perfil.jpg",
                            foto.toRequestBody("image/jpeg".toMediaType())
                        )
                        .build()
                } else {
                    ByteArray(0).toRequestBody()
                }
                val peticion = Request.Builder()
                    .url(SupabaseConfig.URL_REGISTRAR_USUARIO)
                    .header("Authorization", "Bearer $token")
                    .post(cuerpo)
                    .build()

                cliente.newCall(peticion).execute().use { respuesta ->
                    if (!respuesta.isSuccessful) {
                        return@withContext Result.failure(IOException("Error del servidor: ${respuesta.code}"))
                    }
                }
                auth.currentUser?.getIdToken(true)?.await()
                Result.success(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun actualizarPerfil(datos: UsuarioUpdateDto): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val token = auth.currentUser?.getIdToken(false)?.await()?.token
                    ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))

                val jsonCuerpo = gson.toJson(datos)
                val cuerpo = jsonCuerpo.toRequestBody("application/json".toMediaTypeOrNull())

                val peticion = Request.Builder()
                    .url("${SupabaseConfig.REST_URL}/usuario?firebase_uid=eq.${auth.currentUser?.uid}")
                    .header("apikey", SupabaseConfig.ANON_KEY)
                    .header("Authorization", "Bearer $token")
                    .patch(cuerpo)
                    .build()

                cliente.newCall(peticion).execute().use { respuesta ->
                    if (!respuesta.isSuccessful) {
                        return@withContext Result.failure(IOException("Error al actualizar perfil: ${respuesta.code}"))
                    }
                    Result.success(Unit)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    //para poder editar el perfil de la persona
    override suspend fun actualizarFotoPerfil(foto: ByteArray): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val token = auth.currentUser?.getIdToken(false)?.await()?.token
                    ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))
                val cuerpo = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "foto",
                        "perfil.jpg",
                        foto.toRequestBody("image/jpeg".toMediaType())
                    )
                    .build()
                val peticion = Request.Builder()
                    .url(SupabaseConfig.URL_ACTUALIZAR_FOTO)
                    .header("Authorization", "Bearer $token")
                    .post(cuerpo)
                    .build()
                cliente.newCall(peticion).execute().use { respuesta ->
                    if (!respuesta.isSuccessful) {
                        return@withContext Result.failure(IOException("Error del servidor: ${respuesta.code}"))
                    }
                    val ruta = JSONObject(respuesta.body?.string().orEmpty()).getString("foto_url")
                    Result.success(ruta)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun obtenerRutaFotoPerfil(): Result<String?> = withContext(Dispatchers.IO) {
        try {
            val usuario = auth.currentUser
                ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))
            val token = usuario.getIdToken(false).await()?.token
                ?: return@withContext Result.failure(IllegalStateException("Token no disponible"))
            val peticion = Request.Builder()
                .url("${SupabaseConfig.REST_URL}/usuario?firebase_uid=eq.${usuario.uid}&select=foto_url")
                .header("apikey", SupabaseConfig.ANON_KEY)
                .header("Authorization", "Bearer $token")
                .get()
                .build()
            cliente.newCall(peticion).execute().use { respuesta ->
                if (!respuesta.isSuccessful) {
                    return@withContext Result.failure(IOException("Error al leer la foto: ${respuesta.code}"))
                }
                val filas = JSONArray(respuesta.body?.string().orEmpty())
                val ruta = if (filas.length() > 0 && !filas.getJSONObject(0).isNull("foto_url"))
                    filas.getJSONObject(0).getString("foto_url") else null
                Result.success(ruta)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun eliminarCuenta(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = auth.currentUser?.getIdToken(false)?.await()?.token
                ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))
            val peticion = Request.Builder()
                .url(SupabaseConfig.URL_ELIMINAR_CUENTA)
                .header("apikey", SupabaseConfig.ANON_KEY)
                .header("Authorization", "Bearer $token")
                .post("{}".toRequestBody("application/json".toMediaType()))
                .build()
            cliente.newCall(peticion).execute().use { respuesta ->
                val texto = respuesta.body?.string().orEmpty()
                if (!respuesta.isSuccessful) {
                    val mensaje = try {
                        JSONObject(texto).optString("message", "Error ${respuesta.code}")
                    } catch (e: Exception) {
                        "Error ${respuesta.code}"
                    }
                    return@withContext Result.failure(IOException(mensaje))
                }
                Result.success(Unit)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun urlFotoPerfil(ruta: String): String =
        "${SupabaseConfig.URL}/storage/v1/object/authenticated/avatares/$ruta"
}