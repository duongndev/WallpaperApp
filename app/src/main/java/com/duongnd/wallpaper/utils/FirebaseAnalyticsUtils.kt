package com.duongnd.wallpaper.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object FirebaseAnalyticsUtils {
    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context.applicationContext)
    }

    fun logEvent(eventName: String, params: Bundle? = null) {
        firebaseAnalytics?.logEvent(eventName, params)
    }

    fun logViewImageDetail(imageId: Int) {
        val bundle = Bundle().apply {
            putInt("image_id", imageId)
        }
        logEvent("view_image_detail", bundle)
    }

    fun logAddFavorite(imageId: Int) {
        val bundle = Bundle().apply {
            putInt("image_id", imageId)
        }
        logEvent("add_favorite", bundle)
    }

    fun logRemoveFavorite(imageId: Int) {
        val bundle = Bundle().apply {
            putInt("image_id", imageId)
        }
        logEvent("remove_favorite", bundle)
    }

    fun logSetWallpaper(target: String) {
        val bundle = Bundle().apply {
            putString("wallpaper_target", target)
        }
        logEvent("set_wallpaper", bundle)
    }

    fun logDownloadImage(imageId: Int) {
        val bundle = Bundle().apply {
            putInt("image_id", imageId)
        }
        logEvent("download_image", bundle)
    }

    fun logShareImage(imageId: Int) {
        val bundle = Bundle().apply {
            putInt("image_id", imageId)
        }
        logEvent("share_image", bundle)
    }

    fun logChangeTheme(theme: String) {
        val bundle = Bundle().apply {
            putString("theme", theme)
        }
        logEvent("change_theme", bundle)
    }
}
