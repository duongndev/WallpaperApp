package com.duongnd.wallpaper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.duongnd.wallpaper.utils.LocaleHelper
import com.duongnd.wallpaper.utils.SpManager
import com.duongnd.wallpaper.utils.getDeviceLanguage
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
@Singleton
class App : Application(), Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var spManager: SpManager

    private var isColdStart = true

    private var firstActivityCreated = false

    companion object {
        var context: Context? = null
        private var mInstance: App? = null
        val instance get() = mInstance
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val locale = getDeviceLanguage()
        val language = spManager.getLanguage()
        LocaleHelper.onAttach(this, language.languageCode)
        super.onConfigurationChanged(newConfig)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        if (isColdStart) {
            isColdStart = false
        }
    }
    override fun attachBaseContext(base: Context?) {
        val context = LocaleHelper.onAttach(base, Locale.getDefault().language)
        super.attachBaseContext(context)
    }
    override fun onActivityDestroyed(p0: Activity) {

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityResumed(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityStarted(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

}