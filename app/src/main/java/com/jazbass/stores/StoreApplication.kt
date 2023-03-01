package com.jazbass.stores

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jazbass.stores.common.database.StoreDatabase

class StoreApplication: Application() {
    companion object{
        lateinit var database: StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database= Room.databaseBuilder(this,
            StoreDatabase::class.java,
            "StoreDatabase")
            .build()

        val MIGRATION_2_3 = object : Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE UNIQUE INDEX index_StoreEntity_name ON StoreEntity (name)")
            }
        }

        database = Room.databaseBuilder(this,
        StoreDatabase::class.java,
        "StoreDatabase")
            .addMigrations(MIGRATION_2_3)
            .build()
    }
}