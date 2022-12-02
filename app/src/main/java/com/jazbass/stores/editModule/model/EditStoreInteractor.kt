package com.jazbass.stores.editModule.model

import com.jazbass.stores.StoreApplication
import com.jazbass.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreInteractor {

    fun saveStore(storeEntity: StoreEntity, callback: (Long) -> Unit){
        doAsync {
            val newId = StoreApplication.database.storeDao().addStore(storeEntity)
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                callback(newId)
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