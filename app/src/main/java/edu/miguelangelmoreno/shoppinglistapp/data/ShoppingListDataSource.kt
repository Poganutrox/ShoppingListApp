package edu.miguelangelmoreno.shoppinglistapp.data

import edu.miguelangelmoreno.shoppinglistapp.domain.service.APIService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShoppingListDataSource @Inject constructor(
    private val api : APIService
) {
    fun getProducts() = flow {
        emit(api.getProducts())
    }
}