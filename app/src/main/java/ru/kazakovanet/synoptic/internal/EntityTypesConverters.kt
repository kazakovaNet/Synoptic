package ru.kazakovanet.synoptic.internal

import androidx.room.TypeConverter

/**
 * Created by NKazakova on 30.06.2020.
 */
object ListToStringConverter {
    @TypeConverter
    @JvmStatic
    public fun fromList(list: List<String>): String = list.joinToString(separator = ",")

    @TypeConverter
    @JvmStatic
    public fun toList(data: String): List<String> = data.split(",")
}