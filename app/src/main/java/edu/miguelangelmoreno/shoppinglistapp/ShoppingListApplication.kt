package edu.miguelangelmoreno.shoppinglistapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import edu.miguelangelmoreno.shoppinglistapp.ui.home.HomeActivity
import edu.miguelangelmoreno.shoppinglistapp.utils.prefs.AddPrefs
import edu.miguelangelmoreno.shoppinglistapp.utils.prefs.UserPrefs

@HiltAndroidApp
class ShoppingListApplication : Application() {
    companion object{
        lateinit var userPrefs: UserPrefs
        lateinit var addPrefs: AddPrefs
    }
    override fun onCreate() {
        super.onCreate()
        userPrefs = UserPrefs(applicationContext)
        addPrefs = AddPrefs(applicationContext)
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