package com.jazbass.stores.mainModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.common.utils.Constants
import com.jazbass.stores.common.utils.StoresException
import com.jazbass.stores.common.utils.TypeError
import com.jazbass.stores.mainModule.model.MainInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var interactor: MainInteractor = MainInteractor()

    private val typeError: MutableLiveData<TypeError> = MutableLiveData()

    private val showProgress: MutableLiveData<Boolean> = MutableLiveData()

    private val stores = interactor.stores

/*    private val stores: MutableLiveData<MutableList<StoreEntity>> by lazy {
        MutableLiveData<MutableList<StoreEntity>>().also {
            loadStores()
        }
    }*/

    fun getTypeError(): MutableLiveData<TypeError> = typeError

    //Devuelve el resultado
    fun getStores(): LiveData<MutableList<StoreEntity>> {
        return stores
    }

    fun isShowProgress(): LiveData<Boolean> {
        return showProgress
    }

    //Interactor es el acceso al Model y le asigna el resultado al stores del MainModel
/*    private fun loadStores(){
     TODO: Review the callback function
//        interactor.getStoresCallback(object : MainInteractor.StoresCallback{
//            override fun getStoresCallback(stores: MutableList<StoreEntity>) {
//                this@MainViewModel.stores.value = stores
//            }
//        })
        showProgress.value = Constants.SHOW

        interactor.getStores {
            showProgress.value = Constants.HIDE
            stores.value = it
            storeList = it
        }
    }*/

    fun deleteStore(storeEntity: StoreEntity) {
        executeAction {
            interactor.deleteStore(storeEntity)
        }
    }

    /** What we sent in delete callback before
     * val index = storeList.indexOf(storeEntity)
     * if (index != -1) {
     * storeList.removeAt(index)
     * stores.value = storeList
    }
     * */

    fun updateStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        executeAction {
            interactor.updateStore(storeEntity)
        }
    }

    //Job is a cancelable coroutine
    private fun executeAction(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            showProgress.value = Constants.SHOW
            try {
                block()
            } catch (e: StoresException) {
                typeError.value = e.typeError
                Log.i("ERROR", "AQUI")
            } finally {
                showProgress.value = Constants.HIDE
            }
        }
    }
}

