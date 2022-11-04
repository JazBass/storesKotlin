package com.jazbass.stores.mainModule.model

import com.jazbass.stores.StoreApplication
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.mainModule.MainActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

    //Abstrae la consulta de datos para poder entregarselos a quien los solicite

    /*interface StoresCallback{
        fun getStoresCallback(stores: MutableList<StoreEntity>)
    }

    fun getStoresCallback(callback: StoresCallback){
        doAsync {
            val storeList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                callback.getStoresCallback(storeList)
            }
        }
    }
     */

    fun getStores(callback: (MutableList<StoreEntity>) -> Unit){
        doAsync {
            val storeList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                callback(storeList)
            }
        }
    }

    fun deleteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }

}