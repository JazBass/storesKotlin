package com.jazbass.stores

import android.app.Application
import androidx.room.Room
import com.jazbass.stores.common.database.StoreAPI
import com.jazbass.stores.common.database.StoreDatabase

class StoreApplication: Application() {
    companion object{
        lateinit var database: StoreDatabase
        lateinit var storeAPI: StoreAPI
    }

    override fun onCreate() {
        super.onCreate()

        database= Room.databaseBuilder(this,
            StoreDatabase::class.java,
            "StoreDatabase")
            .build()

        //Volley
        storeAPI = StoreAPI.getInstance(this)
            /**
             * Aqui le pasamos el contexto de Application, es mejor que de el de la MainActivity ya
             * que no depende del ciclo de vida de la app
             * */
    }
}