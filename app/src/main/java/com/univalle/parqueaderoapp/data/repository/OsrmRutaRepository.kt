package com.univalle.parqueaderoapp.data.repository

import com.univalle.parqueaderoapp.data.ubicacion.Coordenada
import com.univalle.parqueaderoapp.domain.model.Ruta
import com.univalle.parqueaderoapp.domain.model.RutaError
import com.univalle.parqueaderoapp.domain.repository.RutaRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OsrmRutaRepository(
    private val cliente: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .build()
) : RutaRepository {

    override suspend fun calcular(origen: Coordenada, destino: Coordenada): Result<Ruta> {
        val url = "$BASE/route/v1/driving/" +
            "${origen.longitud},${origen.latitud};${destino.longitud},${destino.latitud}" +
            "?overview=full&geometries=geojson"
        val peticion = Request.Builder().url(url).header("User-Agent", USER_AGENT).build()

        return try {
            cliente.newCall(peticion).esperar().use { respuesta ->
                interpretar(respuesta.code, respuesta.body?.string().orEmpty())
            }
        } catch (e: IOException) {
            Result.failure(RutaError.SinConexion())
        }
    }

    private fun interpretar(codigoHttp: Int, cuerpo: String): Result<Ruta> {
        if (codigoHttp == 429) return Result.failure(RutaError.LimiteExcedido())
        return try {
            val json = JSONObject(cuerpo)
            when (json.optString("code")) {
                "Ok" -> {
                    val ruta = json.getJSONArray("routes").getJSONObject(0)
                    val coordenadas = ruta.getJSONObject("geometry").getJSONArray("coordinates")
                    // GeoJSON entrega [longitud, latitud]
                    val puntos = List(coordenadas.length()) { i ->
                        val par = coordenadas.getJSONArray(i)
                        Coordenada(latitud = par.getDouble(1), longitud = par.getDouble(0))
                    }
                    Result.success(
                        Ruta(puntos, ruta.getDouble("distance"), ruta.getDouble("duration"))
                    )
                }
                "NoRoute", "NoSegment" -> Result.failure(RutaError.SinRuta())
                else -> Result.failure(RutaError.Servicio(codigoHttp))
            }
        } catch (e: JSONException) {
            Result.failure(RutaError.Servicio(codigoHttp))
        }
    }

    private suspend fun Call.esperar(): Response = suspendCancellableCoroutine { c ->
        c.invokeOnCancellation { cancel() }
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (c.isActive) c.resumeWithException(e)
            }
            override fun onResponse(call: Call, response: Response) = c.resume(response)
        })
    }

    private companion object {
        const val BASE = "https://router.project-osrm.org"
        const val USER_AGENT = "ParkTulua/1.0 (proyecto academico Univalle)"
    }
}