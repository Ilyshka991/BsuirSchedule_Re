package com.pechuro.bsuirschedule.feature.main.addschedule.fragment

import com.pechuro.bsuirschedule.di.annotations.ChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddScheduleFragmentProvider {

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bind(): AddScheduleFragment
}