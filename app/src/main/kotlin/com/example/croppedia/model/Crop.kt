package com.example.croppedia.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.croppedia.utils.Constants.CROP_TABLE
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = CROP_TABLE)
data class Crop(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val type: String,
    val image: String,
    val about: String,
    val season: String,
    @ColumnInfo(name = "climate_requirements")
    val climateRequirements: List<String>
) : Parcelable
