package edu.miguelangelmoreno.shoppinglistapp.data.repository

import edu.miguelangelmoreno.shoppinglistapp.data.datasource.APIDataSource
import edu.miguelangelmoreno.shoppinglistapp.model.User
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dataSource: APIDataSource
){
    suspend fun checkAccess(user: User): Response<User> = dataSource.checkAccess(user)
    suspend fun createUser(user: User): Response<User> = dataSource.createUser(user)
    suspend fun updateUser(user: User): Response<User> = dataSource.updateUser(user)
}