package com.univalle.parqueaderoapp.data.vehiculos

import android.view.PixelCopy.Request
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
class SupabaseVehiculoRepository (
    private val auth: FirebaseAuth,
    private val cliente: OkHttpClient,
) : VehiculoRepository{
    override suspend fun obtenerVehiculosUsuario(): Result<List<VehiculoEntity>> {
        val usuarioActual = auth.currentUser

        if(usuarioActual != null){
            val token = usuarioActual.getIdToken(false).await().token

            val peticion = Request.Builder()
                .url("https://vphjvtrjmhdhozxhtww.supabase.co/rest/v1/vehiculo?select=*")
        }
        else{
            return Result.failure(IllegalStateException("No hay sesion activa"))
        }
    }
}