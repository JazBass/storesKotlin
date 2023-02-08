package com.jazbass.stores.editModule.model

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.jazbass.stores.StoreApplication
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.common.utils.StoresException
import com.jazbass.stores.common.utils.TypeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditStoreInteractor {

    fun getStoreById(id: Long): LiveData<StoreEntity> {
        return StoreApplication.database.storeDao().getStoreById(id)
    }

    suspend fun saveStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO) {
        try {
            StoreApplication.database.storeDao().addStore(storeEntity)
        } catch (e: SQLiteConstraintException) {
            throw StoresException(TypeError.INSERT)
        }
    }

    suspend fun updateStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO) {
        try {
            StoreApplication.database.storeDao().updateStore(storeEntity)
        } catch (e: SQLiteConstraintException) {
            throw StoresException(TypeError.UPDATE)
        }
    }
}