package com.univalle.parqueaderoapp.data.supabase

object SupabaseConfig {
    val URL: String = com.univalle.parqueaderoapp.BuildConfig.SUPABASE_URL
    val ANON_KEY: String = com.univalle.parqueaderoapp.BuildConfig.SUPABASE_ANON_KEY

    val REST_URL: String = "$URL/rest/v1"
}