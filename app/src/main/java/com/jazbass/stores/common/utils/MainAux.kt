package com.jazbass.stores.common.utils

import com.jazbass.stores.common.entities.StoreEntity

interface MainAux {
    fun hideFab(isVisible: Boolean = false)
    fun addStore(storeEntity: StoreEntity)
    fun updateStore(storeEntity: StoreEntity)
}