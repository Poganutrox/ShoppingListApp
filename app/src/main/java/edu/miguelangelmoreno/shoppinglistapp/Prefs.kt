package edu.miguelangelmoreno.shoppinglistapp

import android.content.Context
import android.content.SharedPreferences
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.PREFS_USERDATA
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_USER_EMAIL
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_USER_ID
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_USER_LASTNAME
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_USER_NAME
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_USER_PHONE
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_USER_REMEMBER
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.SHARE_USER_TOKEN

class Prefs (context : Context) {
    private val storage: SharedPreferences = context.getSharedPreferences(PREFS_USERDATA, 0)

    fun saveUser(user: User){
        storage.edit().putString(SHARE_USER_TOKEN, user.token).apply()
        user.id?.let { storage.edit().putInt(SHARE_USER_ID, it).apply() }
        storage.edit().putString(SHARE_USER_NAME, user.name).apply()
        storage.edit().putString(SHARE_USER_LASTNAME, user.lastName).apply()
        storage.edit().putString(SHARE_USER_EMAIL, user.email).apply()
        storage.edit().putString(SHARE_USER_PHONE, user.phone).apply()
    }

    fun getToken() : String? = storage.getString(SHARE_USER_TOKEN, null)
    fun removeToken(){
        storage.edit().remove(SHARE_USER_TOKEN).apply()
    }
    fun getLoggedUser() : User {
        val token = storage.getString(SHARE_USER_TOKEN, null)
        val id = storage.getInt(SHARE_USER_ID, -1)
        val name = storage.getString(SHARE_USER_NAME, null)
        val lastName = storage.getString(SHARE_USER_LASTNAME, null)
        val email = storage.getString(SHARE_USER_EMAIL, null)
        val phone = storage.getString(SHARE_USER_PHONE, null)

        return User(token, id, name, lastName, phone, email)
    }

    fun updateLoggedUser(user: User){
        storage.edit().putString(SHARE_USER_NAME, user.name).apply()
        storage.edit().putString(SHARE_USER_LASTNAME, user.lastName).apply()
        storage.edit().putString(SHARE_USER_EMAIL, user.email).apply()
        storage.edit().putString(SHARE_USER_PHONE, user.phone).apply()
    }

    fun clear(){
        storage.edit().clear().apply()
    }

    fun setRemember(remember : Boolean){
        storage.edit().putBoolean(SHARE_USER_REMEMBER, remember).apply()
    }
    fun isRemember() : Boolean{
        return storage.getBoolean(SHARE_USER_REMEMBER, false)
    }
}