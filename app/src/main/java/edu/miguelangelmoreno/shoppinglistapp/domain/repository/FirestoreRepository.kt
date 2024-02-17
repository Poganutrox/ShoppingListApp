package edu.miguelangelmoreno.shoppinglistapp.domain.repository

import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseFirestoreResponse
import edu.miguelangelmoreno.shoppinglistapp.model.User

interface FirestoreRepository {
    suspend fun insertUser(user: User): FirebaseFirestoreResponse
}