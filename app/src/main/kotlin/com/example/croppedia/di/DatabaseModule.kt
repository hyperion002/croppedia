package com.example.croppedia.di

import android.content.Context
import androidx.room.Room
import com.example.croppedia.data.local.CropDatabase
import com.example.croppedia.utils.Constants.CROP_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CropDatabase {
        return Room.databaseBuilder(
            context,
            CropDatabase::class.java,
            CROP_DATABASE
        ).build()
    }

    @Singleton
    @Provides
    fun provideDao(database: CropDatabase) = database.cropDao()
}