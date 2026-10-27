package com.duongnd.wallpaper.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.duongnd.wallpaper.data.database.AppDatabase
import com.duongnd.wallpaper.data.database.ImageDao
import com.duongnd.wallpaper.utils.SpManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSpManager(@ApplicationContext context: Context): SpManager {
        return SpManager.getInstance(context)
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "myapp.db").build()
    }

    @Provides
    @Singleton
    fun provideImageDao(db: AppDatabase): ImageDao = db.imagesDao()

}
