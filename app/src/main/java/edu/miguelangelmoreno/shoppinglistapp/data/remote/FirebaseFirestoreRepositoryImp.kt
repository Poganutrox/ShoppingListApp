package edu.miguelangelmoreno.shoppinglistapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseAuthResponse
import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseFirestoreResponse
import edu.miguelangelmoreno.shoppinglistapp.domain.repository.FirestoreRepository
import edu.miguelangelmoreno.shoppinglistapp.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreRepository {
    override suspend fun insertUser(user: User): FirebaseFirestoreResponse {
        val colUsers = firestore.collection("Users")
        val newUser = hashMapOf(
            "name" to user.name,
            "last_name" to user.lastName,
            "email" to user.email,
            "phone_number" to user.phone
        )

        return try {
            colUsers.document().set(newUser).await()
            FirebaseFirestoreResponse.Success
        }catch (e: Exception) {
            FirebaseFirestoreResponse.Error(e.message ?: "Unknown error inserting the user")
        }
    }
}