package com.duongnd.wallpaper.utils

import android.app.Activity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.duongnd.wallpaper.BuildConfig
import com.duongnd.wallpaper.data.model.AdConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson


class FirebaseConfigManager {

    companion object {
        const val KEY_ADS_STATUS_ENABLE = "ad_config"
        const val KEY_DISABLE_ALL_ADS = "disable_all_ads"
        const val KEY_DISABLE_ALL_ADS_BY_VERSION = "disable_all_ads_by_version"

        const val KEY_VERSION_FORCE = "version_force"
        const val KEY_IS_FORCE_UPDATE = "is_force_update"

        private var mInstance: FirebaseConfigManager = FirebaseConfigManager()
        fun instance(): FirebaseConfigManager {
            return mInstance
        }
    }

    private val mMaxTryTime = 6
    private var mTryTime = 0

    private var isFetchDone = false
    private val pendingActions: MutableList<() -> Unit> = mutableListOf()

    var versionForce = "1.0.0"
    var isForceUpdate = false
    var isDisableAllAds = false
    var disableAllAdsByVersion = ""


    var adConfig: AdConfig = AdConfig()

    fun doAfterFetch(action: () -> Unit) {
        if (isFetchDone) {
            action()
        } else {
            pendingActions.add(action)
        }
    }

    fun doAfterFetch(owner: LifecycleOwner, action: () -> Unit) {
        val lifecycle = owner.lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) return
        lateinit var wrappedAction: () -> Unit
        val observer = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                pendingActions.remove(wrappedAction)
            }
        }
        wrappedAction = {
            lifecycle.removeObserver(observer)
            val isFinishing = (owner as? Activity)?.isFinishing == true
            if (lifecycle.currentState != Lifecycle.State.DESTROYED && !isFinishing) {
                action()
            }
        }
        lifecycle.addObserver(observer)
        doAfterFetch(wrappedAction)
    }

    fun fetch(onFetchComplete: (() -> Unit)? = null) {
        val longCache = 15 * 60L
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetch(longCache)
            .continueWithTask { remoteConfig.activate() }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.e("load config success")
                    if (readConfig(remoteConfig)) {
                        mTryTime = 0
                        onFetchFinished(onFetchComplete)
                    } else {
                        tryFetchAgain(onFetchComplete)
                    }
                } else {
                    Logger.e("load config fail")
                    tryFetchAgain(onFetchComplete)
                }
            }
    }

    private fun readConfig(remoteConfig: FirebaseRemoteConfig): Boolean {
        isDisableAllAds = remoteConfig.getBoolean(KEY_DISABLE_ALL_ADS)
        disableAllAdsByVersion = remoteConfig.getString(KEY_DISABLE_ALL_ADS_BY_VERSION)
        versionForce = remoteConfig.getString(KEY_VERSION_FORCE)
        isForceUpdate = remoteConfig.getBoolean(KEY_IS_FORCE_UPDATE)
        val json = remoteConfig.getString(KEY_ADS_STATUS_ENABLE)
        Logger.e("FirebaseConfigManager : $json")

        val isValid = json.isNotEmpty() && try {
            adConfig = Gson().fromJson(json, AdConfig::class.java)
            true
        } catch (e: Exception) {
            Logger.e(e.message)
            false
        }

        return isValid
    }

    private fun shouldDisableAllAds(): Boolean {
        if (isDisableAllAds) return true
        return disableAllAdsByVersion.split(",")
            .map { it.trim() }
            .any { it.isNotEmpty() && it == BuildConfig.VERSION_NAME }
    }

    private fun onFetchFinished(onFetchComplete: (() -> Unit)?) {
        if (isFetchDone) return
        isFetchDone = true
        onFetchComplete?.invoke()
        val actions = pendingActions.toList()
        pendingActions.clear()
        actions.forEach { it() }
    }

    private fun tryFetchAgain(onFetchComplete: (() -> Unit)? = null) {
        ++mTryTime
        if (mTryTime < mMaxTryTime) {
            fetch(onFetchComplete)
        } else {
            mTryTime = 0
            readConfig(FirebaseRemoteConfig.getInstance())
            onFetchFinished(onFetchComplete)
        }
    }
}
