package com.univalle.parqueaderoapp.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository{
    override suspend fun iniciarSesion(email: String, contraseña: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, contraseña).await()
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun iniciarSesionConGoogle(idToken: String) : Result<Boolean>{
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun iniciarSesionInvitado(): Result<Boolean> {
        return try {
            if (auth.currentUser == null) auth.signInAnonymously().await()
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun registrarConCorreo(
        email: String,
        contrasena : String,
        nombre: String
    ): Result<Boolean>{
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, contrasena).await()
            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(nombre)
                .build()
            authResult.user?.updateProfile(profileUpdates)?.await()
            authResult.user?.sendEmailVerification()?.await()
            authResult.user?.getIdToken(true)?.await()
            Result.success(true)
        } catch (e : CancellationException){
            throw e
        } catch (e : Exception){
            Result.failure(e)
        }
    }

    override suspend fun recuperarPassword(email: String): Result<Unit>{
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun reenviarVerificacion(): Result<Unit> {
        return try {
            val usuario = auth.currentUser
                ?: return Result.failure(IllegalStateException("Sin sesión activa"))
            usuario.sendEmailVerification().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verificarCorreo(): Result<Boolean> {
        return try {
            val usuario = auth.currentUser
                ?: return Result.failure(IllegalStateException("Sin sesión activa"))
            usuario.reload().await()
            // El token viejo sigue diciendo "sin verificar": hay que pedir uno nuevo
            if (usuario.isEmailVerified) usuario.getIdToken(true).await()
            Result.success(usuario.isEmailVerified)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override fun uidActual(): String? = auth.currentUser?.uid
    override fun cerrarSesion() {
        auth.signOut()
    }
}