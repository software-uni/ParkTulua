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
import java.io.IOException

interface UsuarioRepository {
    suspend fun sincronizarUsuario(foto : ByteArray?= null): Result<Unit>
    suspend fun actualizarPerfil(datos : UsuarioUpdateDto) : Result<Unit>
    fun obtenerUrlFotoPerfil(uid: String): String
}

class SupabaseUsuarioRepository(
    private val auth: FirebaseAuth,
    private val cliente: OkHttpClient,
    private val urlFuncion: String,

) : UsuarioRepository {

    override suspend fun sincronizarUsuario(foto : ByteArray?): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = auth.currentUser?.getIdToken(false)?.await()?.token
                ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))

            val cuerpo = if (foto != null) {
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("foto", "perfil.jpg", foto.toRequestBody("image/jpeg".toMediaType()))
                    .build()
            } else {
                ByteArray(0).toRequestBody()
            }
            val peticion = Request.Builder()
                .url(urlFuncion)
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

    //para poder editar el perfil de la persona
    override suspend fun actualizarPerfil(datos: UsuarioUpdateDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val usuarioActual = auth.currentUser
                ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))

            val token = usuarioActual.getIdToken(false).await()?.token
                ?: return@withContext Result.failure(IllegalStateException("Token no disponible"))

            val uid = usuarioActual.uid

            val jsonBody = Gson().toJson(datos)
            val cuerpo = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

            val url = "${SupabaseConfig.REST_URL}/usuario?firebase_uid=eq.$uid"

            // 3. Hacemos la petición PATCH
            val peticion = Request.Builder()
                .url(url)
                .header("apikey", SupabaseConfig.ANON_KEY) // Supabase requiere el anon_key
                .header("Authorization", "Bearer $token")
                .header("Prefer", "return=minimal")
                .patch(cuerpo)
                .build()

            cliente.newCall(peticion).execute().use { respuesta ->
                if (!respuesta.isSuccessful) {
                    return@withContext Result.failure(IOException("Error al actualizar: ${respuesta.code}"))
                }
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun obtenerUrlFotoPerfil(uid: String): String {
        return "${SupabaseConfig.URL}/storage/v1/object/public/avatares/$uid/perfil.jpg"
    }
}