package com.threedee.cache.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        private val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun toDouble(data: String?): List<Double> {
            if (data == null) {
                return emptyList()
            }
            val listType = object : TypeToken<List<Double>>(){}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromDouble(double: List<Double>): String {
            return gson.toJson(double)
        }
    }
}