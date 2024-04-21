package edu.miguelangelmoreno.shoppinglistapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import edu.miguelangelmoreno.shoppinglistapp.ui.home.HomeActivity

@HiltAndroidApp
class ShoppingListApplication : Application() {
    companion object{
        lateinit var prefs:Prefs
    }
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        checkUserValues()
    }

    private fun checkUserValues() {
        if (prefs.isRemember() && !prefs.getToken().isNullOrEmpty()) {
            HomeActivity.navigate(applicationContext)
        } else {
            prefs.clear()
        }
    }

}