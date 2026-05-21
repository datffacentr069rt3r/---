package com.deafsmartglasses1.app.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun softTap(context: Context) {
    vibrate(context, durationMs = 8, amplitude = 38)
}

fun mediumTap(context: Context) {
    vibrate(context, durationMs = 12, amplitude = 74)
}

fun selectionTap(context: Context) {
    vibrate(context, durationMs = 7, amplitude = 52)
}

fun successTap(context: Context) {
    val vibrator = context.getSystemService(Vibrator::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 10, 35, 18),
                intArrayOf(0, 70, 0, 105),
                -1
            )
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(35)
    }
}

private fun vibrate(
    context: Context,
    durationMs: Long,
    amplitude: Int
) {
    val vibrator = context.getSystemService(Vibrator::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(
            VibrationEffect.createOneShot(
                durationMs,
                amplitude.coerceIn(1, 255)
            )
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(durationMs)
    }
}