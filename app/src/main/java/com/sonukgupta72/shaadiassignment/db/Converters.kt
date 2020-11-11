package com.sonukgupta72.shaadiassignment.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sonukgupta72.shaadiassignment.network.ProfileResponseDataModule

class Converters {
    private val gson = Gson()
    @TypeConverter
    fun stringObject(data: String?): ProfileResponseDataModule.ProfileDataModel {
        val listType = object : TypeToken<ProfileResponseDataModule.ProfileDataModel>() {
        }.type

        return gson.fromJson<ProfileResponseDataModule.ProfileDataModel>(data, listType)
    }

    @TypeConverter
    fun objectToString(someObjects: ProfileResponseDataModule.ProfileDataModel): String {
        return gson.toJson(someObjects).toString()
    }
}