package com.duongnd.wallpaper.utils

import com.duongnd.wallpaper.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

object RemoteConfigManager {
    const val KEY_MIN_REQUIRED_VERSION = "min_required_version"
    const val KEY_IS_FORCE_UPDATE_ENABLED = "is_force_update_enabled"

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig.apply {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
            }
            setConfigSettingsAsync(configSettings)

            val defaults = mapOf<String, Any>(
                KEY_MIN_REQUIRED_VERSION to BuildConfig.VERSION_NAME,
                KEY_IS_FORCE_UPDATE_ENABLED to false
            )
            setDefaultsAsync(defaults)
        }
    }

    /**
     * Fetches and activates Remote Config values.
     * Executes [onComplete] with a boolean indicating whether the fetch/activate succeeded.
     */
    fun fetchAndActivate(onComplete: ((Boolean) -> Unit)? = null) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                onComplete?.invoke(task.isSuccessful)
            }
    }

    fun getMinRequiredVersion(): String {
        return remoteConfig.getString(KEY_MIN_REQUIRED_VERSION).ifEmpty { BuildConfig.VERSION_NAME }
    }

    fun isForceUpdateEnabled(): Boolean {
        return remoteConfig.getBoolean(KEY_IS_FORCE_UPDATE_ENABLED)
    }
}
