package com.duongnd.wallpaper.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.duongnd.wallpaper.BuildConfig
import com.duongnd.wallpaper.presentation.dialog.ForceUpdateDialog

object Utils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun fullScreenImmersive(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showAlertDialog(
        context: Context?,
        strTitle: String?,
        strText: String?,
        buttonOkText: String?,
        buttonCancelText: String?,
        cancelable: Boolean,
        okListener: DialogInterface.OnClickListener?,
        cancelListener: DialogInterface.OnClickListener?
    ) {
        if (context is Activity && !context.isFinishing) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle(strTitle)
            alertDialogBuilder.setMessage(strText)
            alertDialogBuilder.setPositiveButton(buttonOkText, okListener)
            alertDialogBuilder.setNegativeButton(buttonCancelText, cancelListener)
            alertDialogBuilder.setCancelable(cancelable)
            val alertDialog: Dialog = alertDialogBuilder.create()

            if (!context.isDestroyed) {
                alertDialog.show()
            }
        } else {
            Log.e("showAlertDialog", "Context is not valid or Activity is not running.")
        }
    }

    fun isConnected(context: Context): Boolean {
        return NetworkConnectivityUtils.isConnected(context)
    }

    fun isPermissionGranted(context: Context?, permission: String?): Boolean {
        return if (context == null || TextUtils.isEmpty(permission)) false else ContextCompat.checkSelfPermission(
            context,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Compares current app version name with minimum required version.
     * Returns true if [currentVersion] is lower than [minRequiredVersion].
     */
    fun isVersionLower(currentVersion: String, minRequiredVersion: String): Boolean {
        if (minRequiredVersion.isBlank()) return false
        return try {
            val currentParts = currentVersion.split(".").map { it.toIntOrNull() ?: 0 }
            val requiredParts = minRequiredVersion.split(".").map { it.toIntOrNull() ?: 0 }
            val maxLength = maxOf(currentParts.size, requiredParts.size)

            for (i in 0 until maxLength) {
                val currentPart = currentParts.getOrElse(i) { 0 }
                val requiredPart = requiredParts.getOrElse(i) { 0 }

                if (currentPart < requiredPart) return true
                if (currentPart > requiredPart) return false
            }
            false
        } catch (_: Exception) {
            false
        }
    }

    fun checkForceUpdate(context: Context, isForceUpdate: Boolean, versionForce: String) {
        if (context !is Activity || context.isFinishing || context.isDestroyed) return

        RemoteConfigManager.fetchAndActivate {
            if (context.isFinishing || context.isDestroyed) return@fetchAndActivate

            val currentVersion = BuildConfig.VERSION_NAME
            val isVersionOutdated = isVersionLower(currentVersion, versionForce)
            if (isForceUpdate || isVersionOutdated) {
                ForceUpdateDialog(context).apply {
                    onExit = {
                        context.finishAffinity()
                    }
                    show()
                }
            }
        }
    }
}
