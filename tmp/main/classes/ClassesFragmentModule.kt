package com.pechuro.bsuirschedule.feature.main.classes

import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter.ClassesViewTypes.EMPLOYEE_DAY
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter.ClassesViewTypes.EMPLOYEE_WEEK
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter.ClassesViewTypes.STUDENT_DAY
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter.ClassesViewTypes.STUDENT_WEEK
import dagger.Module
import dagger.Provides

@Module
class ClassesFragmentModule {

    @Provides
    @FragmentScope
    fun providePagerAdapter(fragment: ClassesFragment) =
            ClassesPagerAdapter(fragment.childFragmentManager)

    @Provides
    @FragmentScope
    fun provideRvPool() = RecyclerView.RecycledViewPool().apply {
        setMaxRecycledViews(STUDENT_DAY, 25)
        setMaxRecycledViews(STUDENT_WEEK, 30)
        setMaxRecycledViews(EMPLOYEE_DAY, 20)
        setMaxRecycledViews(EMPLOYEE_WEEK, 20)
    }
}