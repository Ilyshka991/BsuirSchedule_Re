package com.pechuro.bsuirschedule.ext

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.pechuro.bsuirschedule.R

private const val FAST_ANIMATION_DURATION_MS: Long = 150
private const val DEFAULT_ANIMATION_DURATION_MS: Long = 250

private val accelerateInterpolator = AccelerateInterpolator()

fun View.animateAlpha(fromAlpha: Float, toAlpha: Float): ValueAnimator = ObjectAnimator
        .ofFloat(this, "alpha", fromAlpha, toAlpha)
        .apply {
            setTag(R.id.tagCurrentAnimator, this)
            duration = FAST_ANIMATION_DURATION_MS
            interpolator = accelerateInterpolator
        }