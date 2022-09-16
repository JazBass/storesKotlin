package com.jazbass.stores

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

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
    }
}