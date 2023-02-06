package com.jazbass.stores.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.editModule.model.EditStoreInteractor
import kotlinx.coroutines.launch

class EditStoreViewModel: ViewModel() {

    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    private val storeSelectedId = MutableLiveData<Long>()

    private val interactor : EditStoreInteractor

    init {
        interactor = EditStoreInteractor()
    }

    fun setStoreSelectedId(id: Long){
        storeSelectedId.value = id
    }

    fun getStoreSelected(): LiveData<StoreEntity>{
        return interactor.getStoreById(storeSelectedId.value!!)
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
        interactor.saveStore(storeEntity) { newId ->
            result.value = newId
        }
    }

    fun updateStore(storeEntity: StoreEntity){
        viewModelScope.launch {
            interactor.updateStore(storeEntity)
        }
    }

}