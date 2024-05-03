package edu.miguelangelmoreno.shoppinglistapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.miguelangelmoreno.shoppinglistapp.data.database.ShoppingListDatabase
import edu.miguelangelmoreno.shoppinglistapp.data.service.ShoppingListDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShoppingListDatabase {
        return Room.databaseBuilder(
            context,
            ShoppingListDatabase::class.java,
            "shoppingList_App_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideShoppingListDAO(database: ShoppingListDatabase) = database.shoppingListDAO()

}
