package edu.miguelangelmoreno.shoppinglistapp.domain.service

import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseFirestoreResponse
import edu.miguelangelmoreno.shoppinglistapp.model.User

interface FirestoreService {
    suspend fun insertUser(user: User): FirebaseFirestoreResponse
}