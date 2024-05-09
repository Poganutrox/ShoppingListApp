package edu.miguelangelmoreno.shoppinglistapp.utils.prefs

import android.content.Context
import android.content.SharedPreferences
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingList
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.PREFS_LIST_ID
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.PREFS_LIST_NAME
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_ADDING_PRODUCTS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddPrefs(context: Context) {
    private val storage: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_ADD_DATA, 0)
    private val isAddingStorage: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_ADDING_DATA, 0)

    private var _addingSize = MutableStateFlow(0)
    val addingSize: StateFlow<Int>
        get() = _addingSize

    init {
        updateAddingSize()
    }

    private fun updateAddingSize() {
        _addingSize.value = storage.all.size
    }

    fun isAdding(): Boolean {
        return isAddingStorage.getBoolean(SHARE_ADDING_PRODUCTS, false)
    }

    fun setIsAdding(isAdding: Boolean) {
        isAddingStorage.edit().putBoolean(SHARE_ADDING_PRODUCTS, isAdding).apply()
    }

    fun setAddingMode(shoppingList: ShoppingList){
        clear()
        setIsAdding(true)
        setProductList(shoppingList)
    }

    fun addProductToList(productId: String, quantity: Int) {
        if (quantity == 0) {
            storage.edit().remove(productId).apply()
        } else {
            storage.edit().putInt(productId, quantity).apply()
        }
        updateAddingSize()
    }

    fun getQuantityByProductId(productId: String): Int {
        return storage.getInt(productId, -1)
    }

    fun getListName() : String? {
        return storage.getString(PREFS_LIST_NAME, "")
    }

    fun getListId() : Int?{
        return storage.getInt(PREFS_LIST_ID, -1)
    }
    fun getAllProductList(): MutableMap<String, Int> {
        val list = storage.all
        var map = mutableMapOf<String, Int>()
        for (key in list.keys) {
            map[key] = list[key] as Int

        }
        return map
    }

    fun removeProduct(productId: String) {
        storage.edit().remove(productId).apply()
        updateAddingSize()
    }

    fun clear() {
        storage.edit().clear().apply()
        updateAddingSize()
    }

    fun setProductList(shoppingList: ShoppingList) {
        val productList = shoppingList.shoppingListProducts
        if (productList != null) {
            for (product in productList) {
                storage.edit().putInt(product.product.id, product.quantity).apply()
            }
            updateAddingSize()
        }
    }
}