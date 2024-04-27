package edu.miguelangelmoreno.shoppinglistapp.data.datasource

import edu.miguelangelmoreno.shoppinglistapp.data.service.ShoppingListDAO
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val room: ShoppingListDAO
) {

}