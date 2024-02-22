package edu.miguelangelmoreno.shoppinglistapp.domain.service

import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseAuthResponse

interface AuthService {
    suspend fun login(email : String, password:String): FirebaseAuthResponse
    suspend fun signUp(email: String, password: String) : FirebaseAuthResponse
}