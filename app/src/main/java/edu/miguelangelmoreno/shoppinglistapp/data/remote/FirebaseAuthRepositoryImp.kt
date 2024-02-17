package edu.miguelangelmoreno.shoppinglistapp.data.remote

import com.google.firebase.auth.FirebaseAuth
import edu.miguelangelmoreno.shoppinglistapp.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception

class FirebaseAuthRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .await()
            true
        }catch (e: Exception){
            false
        }
    }
}