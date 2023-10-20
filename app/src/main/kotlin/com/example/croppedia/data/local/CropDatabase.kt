package com.example.croppedia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.croppedia.model.Crop

@Database(entities = [Crop::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
abstract class CropDatabase : RoomDatabase() {

    abstract fun cropDao(): CropDao
}