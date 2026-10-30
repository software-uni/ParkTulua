package com.univalle.parqueaderoapp

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.core.network.crearCargadorImagenes
import com.univalle.parqueaderoapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }

        coil.Coil.setImageLoader(crearCargadorImagenes(applicationContext, FirebaseAuth.getInstance()))
    }
}
