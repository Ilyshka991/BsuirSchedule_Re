package com.pechuro.bsuirschedule.ui.activity.navigation

import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class NavigationActivityViewModel @Inject constructor(
        private val repository: ScheduleRepository) : BaseViewModel() {
    val command = SingleLiveEvent<ScheduleUpdateStatus>()

    fun updateSchedule(info: ScheduleInformation) =
            compositeDisposable.add(repository.update(info)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        command.call(ScheduleUpdateStatus.OnScheduleUpdateUpdated(info))
                    }, {
                        command.call(ScheduleUpdateStatus.OnScheduleFailUpdate(info))
                    })
            )
}

sealed class ScheduleUpdateStatus {
    class OnScheduleUpdateUpdated(val info: ScheduleInformation) : ScheduleUpdateStatus()
    class OnScheduleFailUpdate(val info: ScheduleInformation) : ScheduleUpdateStatus()
    class OnRequestUpdate(val info: ScheduleInformation) : ScheduleUpdateStatus()
}