package com.univalle.parqueaderoapp.di

import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.auth.AuthRepository
import com.univalle.parqueaderoapp.data.auth.FirebaseAuthRepository
import com.univalle.parqueaderoapp.data.supabase.SupabaseConfig
import com.univalle.parqueaderoapp.data.usuario.FotoPerfilLocal
import com.univalle.parqueaderoapp.data.usuario.SupabaseUsuarioRepository
import com.univalle.parqueaderoapp.data.usuario.UsuarioRepository
import com.univalle.parqueaderoapp.data.vehiculos.SupabaseVehiculoRepository
import com.univalle.parqueaderoapp.data.vehiculos.VehiculoRepository
import com.univalle.parqueaderoapp.ui.auth.screen.AuthViewModel
import com.univalle.parqueaderoapp.ui.perfil.screen.PerfilViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {

    // --- Instancias de Red / Servicios ---
    single { FirebaseAuth.getInstance() }
    single {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }
    single { FotoPerfilLocal(androidContext()) }

    // --- Repositorios ---
    single<AuthRepository> { FirebaseAuthRepository(get()) }
    single<VehiculoRepository> { SupabaseVehiculoRepository(get(), get()) }
    single<UsuarioRepository> {
        SupabaseUsuarioRepository(
            auth = get(),
            cliente = get(),
            urlFuncion = "${SupabaseConfig.URL}/functions/v1/sincronizar_usuario"
        )
    }

    // --- ViewModels ---
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { PerfilViewModel(get(), get(), get()) }
}
