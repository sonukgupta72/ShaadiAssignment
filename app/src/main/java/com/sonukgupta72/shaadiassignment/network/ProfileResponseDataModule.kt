package com.sonukgupta72.shaadiassignment.network

import androidx.room.Embedded
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

data class ProfileResponseDataModule (val results: List<ProfileDataModel>, val info: InfoDataModel) {
    data class ProfileDataModel (val gender: String, val picture: Picture, val name: Name) {
        var accepted: Boolean? = null
    }
    data class InfoDataModel (val seed: String, val result: Int)
    data class Picture (val large: String, val medium: String, val thumbnail: String)
    data class Name (val title: String, val first: String, val last: String)
}