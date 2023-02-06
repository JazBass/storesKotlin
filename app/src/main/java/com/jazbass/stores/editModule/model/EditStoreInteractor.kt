package com.jazbass.stores.editModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.jazbass.stores.StoreApplication
import com.jazbass.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreInteractor {

    fun getStoreById(id: Long): LiveData<StoreEntity> {

        val store : LiveData<StoreEntity> = liveData {
            val storeLiveData = StoreApplication.database.storeDao().getStoreById(id)
            emitSource(storeLiveData)
        }

        return store
    }

    fun saveStore(storeEntity: StoreEntity, callback: (Long) -> Unit){
        doAsync {
            val newId = StoreApplication.database.storeDao().addStore(storeEntity)
            uiThread {
                callback(newId)
            }
        }
    }

    suspend fun updateStore(storeEntity: StoreEntity) {
        StoreApplication.database.storeDao().updateStore(storeEntity)
    }
}