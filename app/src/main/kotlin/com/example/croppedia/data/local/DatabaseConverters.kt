package com.example.croppedia.data.local

import androidx.room.TypeConverter

class DatabaseConverters {
    private val separator = "|"

    @TypeConverter
    fun convertListToString(list: List<String>): String =
        list.joinToString(separator = separator)

    @TypeConverter
    fun convertStringToList(string: String): List<String> =
        string.split(separator)
}