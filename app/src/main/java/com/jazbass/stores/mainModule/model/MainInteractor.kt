package com.jazbass.stores.mainModule.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jazbass.stores.StoreApplication
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.common.utils.Constants
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

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

    fun getStoresRoom(callback: (MutableList<StoreEntity>) -> Unit){
        doAsync {
            val storeList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                val json = Gson().toJson(storeList)
                Log.i("Gson", json)
                callback(storeList)
            }
        }
    }

    fun getStores(callback: (MutableList<StoreEntity>) -> Unit){
        val url =  "https://stores.free.beeceptor.com/my/api/path"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,{res ->
            Log.i("Response", res.toString())

            val status = res.getInt(Constants.STATUS_PROPERTY)
            if (status == Constants.SUCCESS){
                Log.i("Status", "$status")

                val jsonObject = Gson().fromJson<StoreEntity>(
                    res.getJSONArray(Constants.STORES_PROPERTY)
                        .get(0).toString(), StoreEntity::class.java
                )
                Log.i("StoreEntity", jsonObject.toString())

            }
        },{
            it.printStackTrace()
        })

        StoreApplication.storeAPI.addToRequestQueue(jsonObjectRequest)
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