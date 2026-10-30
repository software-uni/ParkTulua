package com.univalle.parqueaderoapp.data.auth

interface AuthRepository {
    suspend fun iniciarSesion(email: String, contraseña: String): Result<Boolean>
    suspend fun iniciarSesionConGoogle(idToken: String) : Result<Boolean>

    suspend fun iniciarSesionInvitado() : Result<Boolean>
    suspend fun registrarConCorreo(email: String, contrasena: String, nombre: String): Result<Boolean>

    suspend fun recuperarPassword(email: String): Result<Unit>
    suspend fun reenviarVerificacion(): Result<Unit>
    suspend fun verificarCorreo(): Result<Boolean>
    fun uidActual(): String?
    suspend fun obtenerToken(): String?
    fun cerrarSesion()
}