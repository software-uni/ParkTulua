package com.univalle.parqueaderoapp.data.usuario

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

interface UsuarioRepository {
    suspend fun sincronizarUsuario(): Result<Unit>
}
class SupabaseUsuarioRepository(
    private val auth: FirebaseAuth,
    private val cliente: OkHttpClient,
    private val urlFuncion: String
) : UsuarioRepository {

    override suspend fun sincronizarUsuario(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = auth.currentUser?.getIdToken(false)?.await()?.token
                ?: return@withContext Result.failure(IllegalStateException("Sin sesión activa"))

            val peticion = Request.Builder()
                .url(urlFuncion)
                .header("Authorization", "Bearer $token")
                .post(ByteArray(0).toRequestBody())
                .build()

            cliente.newCall(peticion).execute().use { respuesta ->
                if (respuesta.isSuccessful) Result.success(Unit)
                else Result.failure(IOException("Error del servidor: ${respuesta.code}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}