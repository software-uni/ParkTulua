package com.univalle.parqueaderoapp.data.auth

interface AuthRepository {
    suspend fun iniciarSesion(email: String, contraseña: String): Result<Boolean>
    suspend fun iniciarSesionConGoogle(idToken: String) : Result<Boolean>

    suspend fun iniciarSesionInvitado() : Result<Boolean>
}