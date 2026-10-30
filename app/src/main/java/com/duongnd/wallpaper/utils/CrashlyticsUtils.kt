package com.duongnd.wallpaper.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUtils {
    
    fun setUserId(userId: String) {
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }

    fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    fun recordException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}