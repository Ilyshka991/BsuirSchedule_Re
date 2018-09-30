package com.pechuro.bsuirschedule.ui.fragment.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentListBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import javax.inject.Inject

class ListFragment : BaseFragment<FragmentListBinding, ListViewModel>() {
    private lateinit var mBinding: FragmentListBinding

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    @Inject
    lateinit var mListAdapter: ListAdapter

    companion object {
        fun newInstance(): ListFragment {
            val args = Bundle()
            val fragment = ListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: ListViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ListViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = mViewDataBinding
        setUp()
        subscribeToLiveData()
    }

    private fun setUp() {
        mLayoutManager.orientation = RecyclerView.VERTICAL
        mBinding.cacheRecyclerView.layoutManager = mLayoutManager
        mBinding.cacheRecyclerView.adapter = mListAdapter
    }

    private fun subscribeToLiveData() {
        mViewModel.listItemsLiveData.observe(this,
                Observer {
                    if (it != null) mViewModel.addItems(it)
                })
    }
}