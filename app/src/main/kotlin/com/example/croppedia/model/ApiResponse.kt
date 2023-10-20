package com.example.croppedia.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val crops: List<Crop> = emptyList(),
)
