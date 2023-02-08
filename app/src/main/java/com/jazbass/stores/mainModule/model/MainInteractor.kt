package com.jazbass.stores.mainModule.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.jazbass.stores.StoreApplication
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.common.utils.StoresException
import com.jazbass.stores.common.utils.TypeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MainInteractor {
    /*
    interface StoresCallback{
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

/*    fun getStores(callback: (MutableList<StoreEntity>) -> Unit) {
        val isLocal = true

        if (isLocal)
            getStoresRoom { storeList -> callback(storeList) }
        else
            getStoresAPI { storeList -> callback(storeList) }
    }*/

/*    private fun getStoresRoom(callback: (MutableList<StoreEntity>) -> Unit) {
        doAsync {
            val storeList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                val json = Gson().toJson(storeList)
                Log.i("Gson", json)
                callback(storeList)
            }
        }
    }
 */

/*    private fun getStoresAPI(callback: (MutableList<StoreEntity>) -> Unit) {

        val url = Constants.STORES_URL + Constants.GET_ALL_PATH
        var storeList = mutableListOf<StoreEntity>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { res ->
            val status = res.optInt(Constants.STATUS_PROPERTY, Constants.ERROR)
            // TODO: Error msg
            if (status == Constants.SUCCESS) {

                val jsonList = res.optJSONArray(Constants.STORES_PROPERTY)?.toString()
                if (jsonList != null) {
                    val mutableListType = object : TypeToken<MutableList<StoreEntity>>() {}.type
                    storeList = Gson().fromJson(jsonList, mutableListType)

                    callback(storeList)
                    return@JsonObjectRequest
                }
            }
            callback(storeList)
        }, {
            it.printStackTrace()
            callback(storeList)
        })

        StoreApplication.storeAPI.addToRequestQueue(jsonObjectRequest)
    }*/

    //Scope is the context which controls the coroutine's lifecycle
    val stores: LiveData<MutableList<StoreEntity>> = liveData {
        delay(1_000)//TEMPORAL For see the progressbar
        val storesLiveData = StoreApplication.database.storeDao().getAllStores()
        emitSource(storesLiveData.map { stores ->
            stores.sortedBy { it.name }.toMutableList()
        })
    }

    suspend fun deleteStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO){
        val result = StoreApplication.database.storeDao().deleteStore(storeEntity)
        if (result == 0) throw StoresException(TypeError.DELETE)
        Log.i("AAAA", ": 1")
    }

    suspend fun updateStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO){
        val result = StoreApplication.database.storeDao().updateStore(storeEntity)
        if (result == 0) throw StoresException(TypeError.UPDATE)
        Log.i("AAAA", ": 2 ")
    }
}