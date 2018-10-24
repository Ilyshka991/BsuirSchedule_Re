package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.impl

import androidx.databinding.ObservableField
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.data.BaseClassesData

class StudentClassesWeekData(subject: String?) : BaseClassesData() {

    val subject = ObservableField<String>()

    init {
        this.subject.set(subject)
    }
}