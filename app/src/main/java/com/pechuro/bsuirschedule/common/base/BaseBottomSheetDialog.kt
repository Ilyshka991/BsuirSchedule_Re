package com.pechuro.bsuirschedule.common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pechuro.bsuirschedule.ext.app
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseBottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onAttach(context: Context) {
        context.app.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    protected fun <T : BaseViewModel> initViewModel(clazz: KClass<T>, shared: Boolean = false): T {
        val owner: ViewModelStoreOwner = if (shared) requireActivity() else this
        return initViewModel(clazz, owner)
    }

    protected fun <T : BaseViewModel> initViewModel(clazz: KClass<T>, owner: ViewModelStoreOwner): T {
        return ViewModelProvider(owner, viewModelFactory).get(clazz.java)
    }
}