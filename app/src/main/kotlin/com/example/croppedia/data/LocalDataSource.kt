package com.example.croppedia.data

import com.example.croppedia.data.local.CropDao
import com.example.croppedia.model.Crop
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val cropDao: CropDao
){
    suspend fun insertCrops(crops: List<Crop>) =
        cropDao.insertCrops(crops)

    fun getAllCrops(): Flow<List<Crop>> =
        cropDao.getAllCrops()
}