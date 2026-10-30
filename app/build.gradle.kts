import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

android {
    namespace = "com.univalle.parqueaderoapp"
    compileSdk {
        version = release(37)
    }
    signingConfigs {
        val keystoreFile = file("/home/kevin/parktulua-key.jks")
        if (keystoreFile.exists()) {
            create("release") {
                storeFile = keystoreFile
                storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD") ?: ""
                keyAlias = localProperties.getProperty("RELEASE_KEY_ALIAS") ?: "parqueadero-release-key"
                keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD") ?: ""
            }
        }
    }

    defaultConfig {
        applicationId = "com.univalle.parqueaderoapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val supabaseUrl = localProperties.getProperty("SUPABASE_URL") ?:""
        val supabaseAnonKey = localProperties.getProperty("SUPABASE_ANON_KEY") ?: ""

        buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"$supabaseAnonKey\"")
    }

    buildTypes {
        release {
            if (signingConfigs.findByName("release") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation("androidx.compose.material:material-icons-extended")
    // Firebase: la BoM mantiene todas las versiones compatibles entre sí
    implementation(platform("com.google.firebase:firebase-bom:34.19.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.airbnb.android:lottie-compose:6.5.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.6.1")
    implementation("io.ktor:ktor-client-android:2.3.12")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation(libs.okhttp)

    // --- Dependencias fusionadas (HEAD + main) ---
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("io.coil-kt:coil-compose:2.6.0") // Mantenemos la versión más reciente de tu rama
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.6.1")
    implementation("io.ktor:ktor-client-android:2.3.12")

    // Para el botón "Continuar con Google"
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Utilidades extra de ambas ramas
    implementation("com.google.code.gson:gson:2.10.1")
    // Seguridad: Almacenamiento local cifrado
    implementation("androidx.security:security-crypto:1.1.0")

    // Koin para Inyección de Dependencias en Jetpack Compose
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")

    // Libreria principal MapLibre Compose
    implementation(libs.maplibre.compose)
    // Renderizador vulkan obligatorio pejecutar Android
    runtimeOnly(libs.maplibre.compose.runtime.vulkan)
    implementation(libs.play.services.location)
}