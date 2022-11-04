package com.jazbass.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jazbass.stores.StoreApplication
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.mainModule.model.MainInteractor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainViewModel: ViewModel() {

    private var storeList: MutableList<StoreEntity>

    //Reflejar los datos dentro de nuestra vista

    private var interactor: MainInteractor

    init {
        storeList = mutableListOf()
        interactor = MainInteractor()
        loadStores()
    }

    private val stores: MutableLiveData<List<StoreEntity>> by lazy {
        MutableLiveData<List<StoreEntity>>().also {
            loadStores()
        }
    }

    //Devuelve el resultado
    fun getStores(): LiveData<List<StoreEntity>>{
        return stores
    }

    //Interactor es el acceso al Model y le asigna el resultado al stores del MainModel
    private fun loadStores(){
//        interactor.getStoresCallback(object : MainInteractor.StoresCallback{
//            override fun getStoresCallback(stores: MutableList<StoreEntity>) {
//                //Esta esc la propiedad que estamos observando
//                this@MainViewModel.stores.value = stores
//            }
//        })
        interactor.getStores {
            stores.value=it
        }
    }

    fun deleteStore(storeEntity: StoreEntity){
        interactor.deleteStore(storeEntity) {
            val index = storeList.indexOf(storeEntity)
            if (index != -1) {
                storeList.removeAt(index)
                stores.value = storeList
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity){
        interactor.updateStore(storeEntity) {
            val index = storeList.indexOf(storeEntity)
            if (index != -1) {
                storeList[index] = storeEntity
                stores.value = storeList
            }
        }
    }

}
