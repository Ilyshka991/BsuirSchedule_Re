package com.pechuro.bsuirschedule.domain.ext

import java.text.DateFormat
import java.text.ParseException
import java.util.*

fun DateFormat.parseOrDefault(date: String, default: Date) = try {
    parse(date) ?: default
} catch (e: ParseException) {
    default
}