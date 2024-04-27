package edu.miguelangelmoreno.shoppinglistapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import edu.miguelangelmoreno.shoppinglistapp.data.database.ShoppingListDatabase
import edu.miguelangelmoreno.shoppinglistapp.ui.home.HomeActivity
import edu.miguelangelmoreno.shoppinglistapp.utils.prefs.FilterPrefs
import edu.miguelangelmoreno.shoppinglistapp.utils.prefs.UserPrefs
import javax.inject.Inject

@HiltAndroidApp
class ShoppingListApplication : Application() {
    companion object{
        lateinit var userPrefs: UserPrefs
        lateinit var filterPrefs: FilterPrefs
    }
    override fun onCreate() {
        super.onCreate()
        userPrefs = UserPrefs(applicationContext)
        filterPrefs = FilterPrefs(applicationContext)
        checkUserValues()
    }

    private fun checkUserValues() {
        if (userPrefs.isRemember() && !userPrefs.getToken().isNullOrEmpty()) {
            HomeActivity.navigate(applicationContext)
        } else {
            userPrefs.clear()
        }
    }

}