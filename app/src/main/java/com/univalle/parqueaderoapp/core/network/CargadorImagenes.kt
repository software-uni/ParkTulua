package com.univalle.parqueaderoapp.core.network

import android.content.Context
import coil.ImageLoader
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.supabase.SupabaseConfig
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


private class TokenSupabaseInterceptor(
    private val auth: FirebaseAuth
) : Interceptor {

    private val hostSupabase = SupabaseConfig.URL.toHttpUrl().host

    override fun intercept(chain: Interceptor.Chain): Response {
        val peticion = chain.request()
        if (peticion.url.host != hostSupabase) return chain.proceed(peticion)

        val token = try {
            auth.currentUser?.let { Tasks.await(it.getIdToken(false)).token }
        } catch (e: Exception) {
            null
        } ?: return chain.proceed(peticion)

        val conToken = peticion.newBuilder()
            .header("apikey", SupabaseConfig.ANON_KEY)
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(conToken)
    }
}

fun crearCargadorImagenes(context: Context, auth: FirebaseAuth): ImageLoader =
    ImageLoader.Builder(context)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor(TokenSupabaseInterceptor(auth))
                .build()
        }
        .build()