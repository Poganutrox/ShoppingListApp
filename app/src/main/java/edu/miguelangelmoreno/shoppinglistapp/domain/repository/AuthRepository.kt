package edu.miguelangelmoreno.shoppinglistapp.domain.repository

import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseAuthResponse

interface AuthRepository {
    suspend fun login(email : String, password:String): FirebaseAuthResponse
    suspend fun signUp(email: String, password: String) : FirebaseAuthResponse
}