package com.jazbass.stores.editModule

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.jazbass.stores.R
import com.jazbass.stores.common.entities.StoreEntity
import com.jazbass.stores.common.utils.TypeError
import com.jazbass.stores.databinding.FragmentEditStoreBinding
import com.jazbass.stores.editModule.viewModel.EditStoreViewModel
import com.jazbass.stores.mainModule.MainActivity

class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    //MVVM
    private lateinit var mEditStoreViewModel: EditStoreViewModel

    private var mActivity: MainActivity? = null
    private var mIsEditMode: Boolean = false
    private lateinit var mStoreEntity: StoreEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mEditStoreViewModel = ViewModelProvider(requireActivity())[EditStoreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()

        setUpTextFields()
    }

    private fun setUpViewModel() {
        mEditStoreViewModel.getStoreSelected().observe(viewLifecycleOwner) {
            mStoreEntity = it ?: StoreEntity()
            if (it != null) {
                mIsEditMode = true
                setUiStore(it)
            } else {
                mIsEditMode = false
            }
            setUpActionBar()
        }

        mEditStoreViewModel.getResult().observe(viewLifecycleOwner){ result ->
            hideKeyword()

            when(result){
                is Long -> {
                    //re assign  ID
                    mStoreEntity.id = result
                    //A new store
                    mEditStoreViewModel.setStoreSelected(mStoreEntity.id)

                    Toast.makeText(
                        mActivity, R.string.edit_store_message_save_success,
                        Toast.LENGTH_SHORT
                    ).show()
                    mActivity?.onBackPressed()
                }
                is StoreEntity -> {
                    mEditStoreViewModel.setStoreSelected(mStoreEntity.id)

                    Toast.makeText( mActivity,
                        R.string.edit_store_message_update_success,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        mEditStoreViewModel.getTypeError().observe(viewLifecycleOwner){typeError ->
            if (typeError !=TypeError.NONE){

                val msgRes = when (typeError){

                    TypeError.GET -> R.string.main_error_get
                    TypeError.INSERT -> R.string.main_error_instert
                    TypeError.UPDATE -> R.string.main_error_update
                    TypeError.DELETE -> R.string.main_error_delete
                    else -> R.string.main_error_unknow

                }
                Snackbar.make(mBinding.root, msgRes, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpTextFields() {
        mBinding.etPhotoUrl.addTextChangedListener {
            Glide.with(this)
                .load(mBinding.etPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mBinding.imagePhoto)
            validateFields(mBinding.tilPhotoUrl)
        }

        mBinding.etName.addTextChangedListener {validateFields(mBinding.tilName)}

        mBinding.etPhone.addTextChangedListener {validateFields(mBinding.tilPhone)}
    }

    private fun setUpActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.edit_store_title)

        setHasOptionsMenu(true)
    }

    private fun setUiStore(storeEntity: StoreEntity) {
        with(mBinding) {
            etName.text = storeEntity.name.editable()
            etPhone.text = storeEntity.phone.editable()
            etWebsite.text = storeEntity.website.editable()
            etPhotoUrl.text = storeEntity.photoUrl.editable()
        }
    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.exit_dialog_title)
                    .setMessage(R.string.exit_dialog_message)
                    .setPositiveButton(R.string.exit_dialog_ok){_, _->
                        if (isEnabled){
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                            mEditStoreViewModel.setShowFab(true)
                        }
                    }
                    .setNegativeButton(R.string.exit_dialog_cancel, null)
                    .show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            R.id.action_save -> {
                if (validateFields(mBinding.tilPhotoUrl, mBinding.tilPhone, mBinding.tilName)) {
                    with(mStoreEntity) {
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        website = mBinding.etWebsite.text.toString().trim()
                        photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                    }

                    if (mIsEditMode) mEditStoreViewModel.updateStore(mStoreEntity)
                    else mEditStoreViewModel.saveStore(mStoreEntity)
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean{
        var isValid= true

        for (textField in textFields){
            if (textField.editText?.text.toString().trim().isEmpty()){
                textField.error = getString(R.string.helper_require)
                isValid = false
            }else textField.error = null
        }

        if (!isValid){
            Snackbar.make(mBinding.root,
                R.string.edit_store_message_valid,
                Snackbar.LENGTH_SHORT).show()
        }

        return isValid
    }

    private fun hideKeyword() {
        val imn = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if(view!=null){
            imn?.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    override fun onDestroyView() {
        hideKeyword()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)

        with(mEditStoreViewModel){
            setResult(Any())
            setTypeError(TypeError.NONE)
        }
        setHasOptionsMenu(false)
        super.onDestroy()
    }
}