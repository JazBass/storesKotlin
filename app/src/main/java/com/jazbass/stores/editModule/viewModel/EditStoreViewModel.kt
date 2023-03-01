package com.jazbass.stores.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.common.utils.StoresException
import com.jazbass.stores.common.utils.TypeError
import com.jazbass.stores.editModule.model.EditStoreInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditStoreViewModel: ViewModel() {

    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    private val storeSelected = MutableLiveData<Long>()

    private val typeError: MutableLiveData<TypeError> = MutableLiveData()

    private val interactor : EditStoreInteractor = EditStoreInteractor()

    fun setTypeError(typeError: TypeError){
        this.typeError.value = typeError
    }

    fun getTypeError(): MutableLiveData<TypeError> = typeError

    fun setStoreSelected(id: Long){
        storeSelected.value = id
    }

    fun getStoreSelected(): LiveData<StoreEntity>{
        return interactor.getStoreById(storeSelected.value!!)
    }
    fun setShowFab(isVisible: Boolean){
        showFab.value = isVisible
    }

    fun getShowFab(): LiveData<Boolean>{
        return showFab
    }
    fun setResult(value: Any){
        result.value = value
    }

    fun getResult(): LiveData<Any>{
        return result
    }

    fun saveStore(storeEntity: StoreEntity){
        executeAction(storeEntity) { interactor.saveStore(storeEntity) }
    }

    fun updateStore(storeEntity: StoreEntity){
        executeAction(storeEntity) { interactor.updateStore(storeEntity) }
    }


    private fun executeAction(storeEntity: StoreEntity, block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                block()
                result.value = storeEntity
            } catch (e: StoresException) {
                typeError.value = e.typeError
            }
        }
    }
}