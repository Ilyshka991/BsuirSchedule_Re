package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.constant.SharedPrefConstants.SCHEDULE_NAME
import com.pechuro.bsuirschedule.constant.SharedPrefConstants.SCHEDULE_TYPE
import com.pechuro.bsuirschedule.databinding.ActivityNavigationBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.adapter.NavItemAdapter
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.activity.settings.SettingsActivity
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragment
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamFragment
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_navigation.*
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>(), HasSupportFragmentInjector, INavigator {

    companion object {
        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)
    }

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    @Inject
    lateinit var mNavAdapter: NavItemAdapter

    override val mViewModel: NavigationActivityViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(NavigationActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_navigation
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setListeners()
        if (savedInstanceState == null) {
            homeFragment()
        }
        subscribeToLiveData()
    }

    override fun onNavigate(info: ScheduleInformation) {
        val argInfo = ScheduleInformation(info.name, info.type)
        val fragment = when (info.type) {
            STUDENT_CLASSES, EMPLOYEE_CLASSES -> ClassesFragment.newInstance(argInfo)
            STUDENT_EXAMS, EMPLOYEE_EXAMS -> ExamFragment.newInstance(argInfo)
            else -> throw IllegalStateException("Invalid type")
        }
        navigate {
            supportFragmentManager.transaction {
                replace(mViewDataBinding.navHostFragment.id, fragment)

                defaultSharedPreferences.edit {
                    putString(SCHEDULE_NAME, info.name)
                    putInt(SCHEDULE_TYPE, info.type)
                }

                setupBottomBar(info.type)
            }
        }
    }

    fun homeFragment() {
        val name = defaultSharedPreferences.getString(SCHEDULE_NAME, "")
        val type = defaultSharedPreferences.getInt(SCHEDULE_TYPE, -1)

        val fragment = if (name.isNullOrEmpty() || type == -1) {
            StartFragment.newInstance()
        } else {
            setupBottomBar(type)

            val argInfo = ScheduleInformation(name!!, type)
            when (type) {
                STUDENT_CLASSES, EMPLOYEE_CLASSES -> ClassesFragment.newInstance(argInfo)
                STUDENT_EXAMS, EMPLOYEE_EXAMS -> ExamFragment.newInstance(argInfo)
                else -> throw IllegalStateException("Invalid type")
            }
        }

        supportFragmentManager.transaction {
            replace(mViewDataBinding.navHostFragment.id, fragment)
        }
    }

    private fun subscribeToLiveData() {
        mViewModel.menuItems.observe(this, Observer {
            mNavAdapter.setItems(it)
        })
    }

    override fun onBackPressed() {
        if (mViewDataBinding.drawerLayout is DrawerLayout &&
                (mViewDataBinding.drawerLayout as DrawerLayout)
                        .isDrawerOpen(GravityCompat.START)) {
            (mViewDataBinding.drawerLayout as DrawerLayout).closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupView() {
        setSupportActionBar(bar)

        mNavAdapter.navigator = this

        mLayoutManager.orientation = RecyclerView.VERTICAL
        mViewDataBinding.navItemList?.layoutManager = mLayoutManager
        mViewDataBinding.navItemList?.adapter = mNavAdapter
    }

    private fun setListeners() {
        if (mViewDataBinding.drawerLayout is DrawerLayout) {
            val toggle = ActionBarDrawerToggle(
                    this, mViewDataBinding.drawerLayout as DrawerLayout, mViewDataBinding.bar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            (mViewDataBinding.drawerLayout as DrawerLayout).addDrawerListener(toggle)
            toggle.syncState()
        }

        mViewDataBinding.navFooterSettings?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        mViewDataBinding.navFooterAddSchedule?.setOnClickListener {
            navigate {
                AddDialog.newInstance().show(supportFragmentManager, "add_dialog")
            }
        }
    }

    private fun setupBottomBar(type: Int) {
        when (type) {
            STUDENT_CLASSES -> {
                mViewDataBinding.barScheduleSubgroup?.visibility = View.VISIBLE
                mViewDataBinding.barScheduleType?.visibility = View.VISIBLE
            }
            EMPLOYEE_CLASSES -> {
                mViewDataBinding.barScheduleSubgroup?.visibility = View.GONE
                mViewDataBinding.barScheduleType?.visibility = View.VISIBLE
            }
            STUDENT_EXAMS, EMPLOYEE_EXAMS -> {
                mViewDataBinding.barScheduleSubgroup?.visibility = View.GONE
                mViewDataBinding.barScheduleType?.visibility = View.GONE
            }
            else -> throw IllegalStateException("Invalid type")
        }
    }

    private fun navigate(action: () -> Unit) {
        if (mViewDataBinding.drawerLayout is DrawerLayout) {
            (mViewDataBinding.drawerLayout as DrawerLayout).addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {

                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerClosed(drawerView: View) {
                    action.invoke()
                    (mViewDataBinding.drawerLayout as DrawerLayout).removeDrawerListener(this)
                }

                override fun onDrawerOpened(drawerView: View) {

                }
            })
            (mViewDataBinding.drawerLayout as DrawerLayout).closeDrawers()
        } else {
            action.invoke()
        }
    }
}