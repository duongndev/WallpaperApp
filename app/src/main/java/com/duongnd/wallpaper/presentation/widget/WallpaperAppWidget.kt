package com.duongnd.wallpaper.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.domain.usecase.GetRandomImageUseCase
import com.duongnd.wallpaper.presentation.preview.activity.PreviewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WallpaperAppWidget : AppWidgetProvider() {

    @Inject
    lateinit var getRandomImageUseCase: GetRandomImageUseCase

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (ACTION_REFRESH_WIDGET == intent.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, WallpaperAppWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            updateWidgets(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val photo = getRandomImageUseCase.execute(GetRandomImageUseCase.Param())

                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context.packageName, R.layout.wallpaper_app_widget)

                    views.setImageViewResource(R.id.ivWidget, photo.imgRes)

                    val previewIntent = Intent(context, PreviewActivity::class.java).apply {
                        putExtra(PreviewActivity.EXTRA_IMAGE, photo)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }

                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        previewIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent)
                    views.setOnClickPendingIntent(R.id.ivWidget, pendingIntent)

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (e: Exception) {
                Log.e("WallpaperAppWidget", "Error updating widget", e)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val ACTION_REFRESH_WIDGET = "com.duongnd.wallpaper.ACTION_REFRESH_WIDGET"

        fun sendRefreshBroadcast(context: Context) {
            val intent = Intent(context, WallpaperAppWidget::class.java).apply {
                action = ACTION_REFRESH_WIDGET
            }
            context.sendBroadcast(intent)
        }
    }
}
