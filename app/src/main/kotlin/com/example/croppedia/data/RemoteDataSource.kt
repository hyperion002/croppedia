package com.example.croppedia.data

import com.example.croppedia.data.remote.CropApi
import com.example.croppedia.model.ApiResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val cropApi: CropApi
){
    suspend fun getAllCrops(): ApiResponse =
        cropApi.getAllCrops()
}