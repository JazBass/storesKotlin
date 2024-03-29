package com.jazbass.stores.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jazbass.stores.common.entities.StoreEntity

@Database(entities = [StoreEntity::class], version = 3)
abstract class StoreDatabase: RoomDatabase() {
    abstract fun storeDao(): StoreDao
}
