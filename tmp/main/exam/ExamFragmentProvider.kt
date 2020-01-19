package com.pechuro.bsuirschedule.feature.main.exam

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ExamFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ExamFragmentModule::class])
    fun provideExamFragmentFactory(): ExamFragment
}