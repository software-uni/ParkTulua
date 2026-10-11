package com.univalle.parqueaderoapp.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository{
    override suspend fun iniciarSesion(email: String, contraseña: String): Result<Boolean> {
        return  try {
            auth.signInWithEmailAndPassword(email, contraseña).await()
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun iniciarSesionConGoogle(idToken: String) : Result<Boolean>{
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}