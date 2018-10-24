package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentListBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.FabCommunication
import com.pechuro.bsuirschedule.ui.activity.navigation.OnFabHide
import com.pechuro.bsuirschedule.ui.activity.navigation.OnFabShowPos
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter
import javax.inject.Inject

class ClassesItemFragment : BaseFragment<FragmentListBinding, ClassesItemViewModel>() {
    companion object {
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ClassesBaseInformation): ClassesItemFragment {
            val args = Bundle()
            args.putParcelable(ARG_INFO, info)

            val fragment = ClassesItemFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    @Inject
    lateinit var mAdapter: ClassesAdapter

    override val mViewModel: ClassesItemViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ClassesItemViewModel::class.java)
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
        loadData()
        setListeners()
    }

    private fun setListeners() {
        mViewDataBinding.recyclerView
                .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val visibleItemCount = mLayoutManager.childCount
                        val totalItemCount = mLayoutManager.itemCount
                        val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                            FabCommunication.publish(if (dy > 0) OnFabHide else OnFabShowPos)
                        }
                    }
                })
    }

    private fun loadData() {
        val info: ClassesBaseInformation? = arguments?.getParcelable(ARG_INFO)
        info?.let { mViewModel.loadData(it) }
    }

    private fun setupView() {
        mLayoutManager.orientation = RecyclerView.VERTICAL
        mViewDataBinding.recyclerView.layoutManager = mLayoutManager
        mViewDataBinding.recyclerView.adapter = mAdapter
    }

    private fun subscribeToLiveData() {
        mViewModel.listItemsLiveData.observe(this,
                Observer {
                    if (it != null) {
                        mAdapter.setItems(it)
                    }
                })
    }
}