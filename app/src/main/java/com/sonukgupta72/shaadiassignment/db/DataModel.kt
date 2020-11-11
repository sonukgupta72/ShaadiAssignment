package com.sonukgupta72.shaadiassignment.db

import androidx.room.*
import com.sonukgupta72.shaadiassignment.network.ProfileResponseDataModule

@Entity
data class DataModel(
    var profileInfo: ProfileResponseDataModule.ProfileDataModel,
    var responseMessage: String?

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}