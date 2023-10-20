package com.example.croppedia.data.remote

import com.example.croppedia.model.ApiResponse
import retrofit2.http.GET

interface CropApi {
    @GET("/crops")
    suspend fun getAllCrops() : ApiResponse
}