package com.example.croppedia.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.croppedia.model.Crop
import kotlinx.coroutines.flow.Flow

@Dao
interface CropDao {

    @Query("SELECT * FROM crop_table ORDER BY id ASC")
    fun getAllCrops(): Flow<List<Crop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrops(crops: List<Crop>)
}