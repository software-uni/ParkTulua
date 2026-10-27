package com.univalle.parqueaderoapp.data.ubicacion

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesUbicacionRepository(context: Context) : UbicacionRepository {
    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)

    override fun gpsActivo(): Boolean {
        val manager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(manager)
    }

    // El permiso lo verifica la pantalla antes de invocar este método
    @SuppressLint("MissingPermission")
    override suspend fun obtenerUbicacionActual(): Result<Coordenada?> =
        suspendCancellableCoroutine { continuacion ->
            val token = CancellationTokenSource()
            continuacion.invokeOnCancellation { token.cancel() }
            try {
                cliente.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, token.token)
                    .addOnSuccessListener { ubicacion ->
                        val coordenada = ubicacion?.let { Coordenada(it.latitude, it.longitude) }
                        continuacion.resume(Result.success(coordenada))
                    }
                    .addOnFailureListener { continuacion.resume(Result.failure(it)) }
            } catch (e: SecurityException) {
                continuacion.resume(Result.failure(e))
            }
        }
}