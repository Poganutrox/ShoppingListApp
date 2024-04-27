package edu.miguelangelmoreno.shoppinglistapp.utils.prefs

import android.content.Context
import android.content.SharedPreferences
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.PREFS_FILTER_DATA
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_FILTER_CATEGORY_ID
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_FILTER_ON_SALE
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_FILTER_PRODUCT_NAME
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_FILTER_SUPERMARKET_IDS

class FilterPrefs (context: Context) {
    private val storage : SharedPreferences = context.getSharedPreferences(PREFS_FILTER_DATA, 0)

    fun getFilterArgs() : Map<String, Any?>{
        val filters = mutableMapOf<String, Any?>()
        val productName = storage.getString(SHARE_FILTER_PRODUCT_NAME, null)
        val categoryId = storage.getInt(SHARE_FILTER_CATEGORY_ID, -1)
        val onSale = storage.getBoolean(SHARE_FILTER_ON_SALE, false)
        val supermarketIds = storage.getStringSet(SHARE_FILTER_SUPERMARKET_IDS, emptySet())?.map { it.toInt() }

        filters[SHARE_FILTER_PRODUCT_NAME] = productName
        filters[SHARE_FILTER_CATEGORY_ID] = categoryId
        filters[SHARE_FILTER_ON_SALE] = onSale
        filters[SHARE_FILTER_SUPERMARKET_IDS] = supermarketIds

        return filters
    }
    fun saveFilterArgs(productName: String?,categoryId: Int?, supermarketIds: Set<Int>?,onSale: Boolean){
        storage.edit().putString(SHARE_FILTER_PRODUCT_NAME, productName).apply()
        if (categoryId != null) {
            storage.edit().putInt(SHARE_FILTER_CATEGORY_ID, categoryId).apply()
        }else{
            storage.edit().putInt(SHARE_FILTER_CATEGORY_ID, -1).apply()
        }
        storage.edit().putBoolean(SHARE_FILTER_ON_SALE, onSale).apply()

        if(!supermarketIds.isNullOrEmpty()){
            storage.edit().putStringSet(SHARE_FILTER_SUPERMARKET_IDS, supermarketIds.map { it.toString() }.toSet())
        }else{
            storage.edit().putStringSet(SHARE_FILTER_SUPERMARKET_IDS, emptySet())
        }

    }
}