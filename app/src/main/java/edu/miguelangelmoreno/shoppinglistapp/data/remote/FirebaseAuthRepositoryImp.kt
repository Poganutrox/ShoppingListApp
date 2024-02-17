package edu.miguelangelmoreno.shoppinglistapp.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseAuthResponse
import edu.miguelangelmoreno.shoppinglistapp.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun login(email: String, password: String): FirebaseAuthResponse {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            FirebaseAuthResponse.Success
        } catch (e: FirebaseAuthInvalidUserException) {
            FirebaseAuthResponse.UserNotExists
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            FirebaseAuthResponse.InvalidCredentials
        } catch (e: Exception) {
            FirebaseAuthResponse.Error("Error: ${e.message}")
        }
    }

    override suspend fun signUp(email: String, password: String): FirebaseAuthResponse {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            FirebaseAuthResponse.Success
        } catch (e: FirebaseAuthUserCollisionException) {
            FirebaseAuthResponse.EmailAlreadyExists
        } catch (e: Exception) {
            FirebaseAuthResponse.Error("Error: ${e.message}")
        }
    }
}
