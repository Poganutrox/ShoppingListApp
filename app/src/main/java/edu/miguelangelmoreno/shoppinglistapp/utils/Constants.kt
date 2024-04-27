package edu.miguelangelmoreno.shoppinglistapp.utils

class Constants {
    companion object {
        const val BASE_URL: String = "http://10.0.2.2:8080/api/"

        const val PREFS_USER_DATA : String = "UserDB"
        const val SHARE_USER_REMEMBER : String = "userRemember"
        const val SHARE_USER_TOKEN : String = "userToken"
        const val SHARE_USER_ID : String = "userId"
        const val SHARE_USER_NAME : String = "username"
        const val SHARE_USER_LASTNAME : String = "userLastname"
        const val SHARE_USER_EMAIL : String = "userEmail"
        const val SHARE_USER_PHONE : String = "userPhone"
        const val SHARE_USER_FAVOURITES : String = "userFavourites"

        const val PREFS_FILTER_DATA : String = "FiltersDB"
        const val SHARE_FILTER_PRODUCT_NAME : String = "productName"
        const val SHARE_FILTER_CATEGORY_ID : String = "categoryId"
        const val SHARE_FILTER_SUPERMARKET_IDS : String = "supermarketIds"
        const val SHARE_FILTER_ON_SALE : String = "onSale"
    }
}
