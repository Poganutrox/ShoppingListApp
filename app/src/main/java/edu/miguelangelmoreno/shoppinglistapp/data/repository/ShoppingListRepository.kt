package edu.miguelangelmoreno.shoppinglistapp.data.repository

import edu.miguelangelmoreno.shoppinglistapp.data.datasource.APIDataSource
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingList
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingListDTO
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val dataSource: APIDataSource
) {
    suspend fun getUserShoppingLists(userId: Int) = dataSource.getUserShoppingLists(userId)
    suspend fun saveShoppingList(shoppingListDTO : ShoppingListDTO) =  dataSource.saveShoppingList(shoppingListDTO)
}