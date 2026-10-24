package com.univalle.parqueaderoapp.data.supabase

object SupabaseConfig {
    val URL: String = com.univalle.parqueaderoapp.BuildConfig.SUPABASE_URL
    val ANON_KEY: String = com.univalle.parqueaderoapp.BuildConfig.SUPABASE_ANON_KEY

    // URL base para las consultas REST
    val REST_URL: String = "$URL/rest/v1"
    // Rutas de las Edge Functions
    val URL_REGISTRAR_USUARIO: String = "$URL/functions/v1/registrar-usuario"
    val URL_ACTUALIZAR_FOTO: String = "$URL/functions/v1/actualizar-foto"

    // Funciones de base de datos (RPC)
    //1. Usuario
    val URL_ELIMINAR_CUENTA : String = "$REST_URL/rpc/eliminar_cuenta"
    //2. Vehiculo
    val URL_REGISTRAR_VEHICULO: String = "$REST_URL/rpc/registrar_vehiculo"
    val URL_QUITAR_VEHICULO: String = "$REST_URL/rpc/quitar_vehiculo"
}