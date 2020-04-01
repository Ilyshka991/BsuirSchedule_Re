package com.pechuro.bsuirschedule.feature.flow

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class FlowFragmentEvent : BaseEvent() {

    object DisplayScheduleSetToday : FlowFragmentEvent()

    object DisplayScheduleGoToDate : FlowFragmentEvent()
}