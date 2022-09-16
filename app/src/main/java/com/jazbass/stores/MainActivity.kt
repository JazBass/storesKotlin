package com.jazbass.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jazbass.stores.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpRecyclerView()
       /* mBinding.btnSave.setOnClickListener {
            val store = StoreEntity(name = mBinding.etName.text.toString().trim())
            Thread {
                StoreApplication.database.storeDao().addStore(store)
            }.start()
            mAdapter.add(store)
        }*/
        mBinding.fab.setOnClickListener{launchEditFragment()}
    }

    private fun launchEditFragment(args: Bundle? = null) {
        //Instanciamos el fragmento que queremos lanzar
        val fragment = EditStoreFragment()
        if (args!=null) fragment.arguments = args
        //gestor y trnsaction
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //Que fragmento y en donde
        with(fragmentTransaction){
            add(R.id.containerMain, fragment)
            addToBackStack(null)
            commit()
        }
        hideFab()
    }

    private fun setUpRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 2)
        getStores()
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getStores() {
        doAsync {
            val stores = StoreApplication.database.storeDao().getAllStores()
            uiThread { mAdapter.setStores(stores) }
        }
    }

    override fun onClick(storeId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)

        launchEditFragment(args)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread { mAdapter.update(storeEntity) }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        doAsync {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread { mAdapter.delete(storeEntity) }
        }
    }

    /*--------------------MainAux---------------------*/

    override fun hideFab(isVisible: Boolean) {
        if(isVisible)mBinding.fab.show()
        else mBinding.fab.hide()
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }
}