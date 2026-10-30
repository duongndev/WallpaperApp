package com.duongnd.wallpaper.presentation.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.domain.usecase.GetRandomImageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log
@AndroidEntryPoint
class WallpaperAppWidget : AppWidgetProvider() {

    @Inject
    lateinit var getRandomImageUseCase: GetRandomImageUseCase

    override fun onUpdate(
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

                    if (photo != null) {
                        Log.d("TAG", "onUpdate: $photo")
                        views.setImageViewResource(R.id.ivWidget, photo.imgRes)
                    } else {
                        Log.d("TAG", "onUpdate: $photo")
                    }

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}