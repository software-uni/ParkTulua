package com.univalle.parqueaderoapp.data.usuario

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

class FotoPerfilLocal(private val context: Context){
    private fun archivo(uid: String) = File(context.filesDir, "foto_pendiente_\$uid.jpg")

    suspend fun guardar(uid: String, uri: Uri): Boolean = withContext(Dispatchers.IO){
        try{
            val peticion = ImageRequest.Builder(context)
                .data(uri)
                .size(LADO_MAXIMO)
                .allowHardware(false)
                .build()
            val resultado = context.imageLoader.execute(peticion) as? SuccessResult
                ?: return@withContext false
            val original = (resultado.drawable as? BitmapDrawable)?.bitmap
                ?: return@withContext false

            val mayor = maxOf(original.width, original.height)
            val bitmap = if (mayor > LADO_MAXIMO) {
                val escala = LADO_MAXIMO.toFloat() / mayor
                Bitmap.createScaledBitmap(
                    original,
                    (original.width * escala).toInt(),
                    (original.height * escala).toInt(),
                    true
                )
            } else original
            val salida = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, CALIDAD_JPEG, salida)
            archivo(uid).writeBytes(salida.toByteArray())
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun leer(uid: String): ByteArray? = withContext(Dispatchers.IO) {
        archivo(uid).takeIf { it.exists() }?.readBytes()
    }

    fun borrar(uid: String) {
        archivo(uid).delete()
    }

    private companion object {
        const val LADO_MAXIMO = 512
        const val CALIDAD_JPEG = 80
    }
}