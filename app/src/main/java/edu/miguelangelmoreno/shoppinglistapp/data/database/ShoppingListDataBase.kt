package edu.miguelangelmoreno.shoppinglistapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.miguelangelmoreno.shoppinglistapp.data.service.ShoppingListDAO
import edu.miguelangelmoreno.shoppinglistapp.entity.PriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.entity.ProductEntity

@Database(
    entities = [PriceHistoryEntity::class, ProductEntity::class],
    version = 1
)
abstract class ShoppingListDatabase : RoomDatabase() {
    abstract fun shoppingListDAO(): ShoppingListDAO
}
