package edu.miguelangelmoreno.shoppinglistapp.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import edu.miguelangelmoreno.shoppinglistapp.domain.repository.FirestoreRepository
import edu.miguelangelmoreno.shoppinglistapp.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreRepository {
    override suspend fun insertUser(user: User): Boolean {
        var isSuccessful = false
        val colUsers = firestore.collection("Users")
        val newUser = hashMapOf(
            "name" to user.name,
            "last_name" to user.lastName,
            "email" to user.email,
            "phone_number" to user.phone
        )

        colUsers.document().set(newUser)
            .addOnSuccessListener { isSuccessful = true }
            .addOnFailureListener { isSuccessful = false }
            .await()
        return isSuccessful
    }

}