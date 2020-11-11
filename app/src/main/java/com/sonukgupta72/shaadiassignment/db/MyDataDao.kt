package com.sonukgupta72.shaadiassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sonukgupta72.shaadiassignment.network.ProfileResponseDataModule
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MyDataDao {
    @Query("Select * From DataModel")
    fun getAllDataModel(): Single<List<DataModel>>

    @Query("Select * From DataModel")
    fun getLiveDataModel(): LiveData<List<DataModel>>

    @Query("UPDATE DataModel SET profileInfo= :profileInfo, responseMessage= :responseMassage  WHERE id= :id")
    fun updateDateModel(profileInfo: ProfileResponseDataModule.ProfileDataModel, responseMassage: String, id: Int): Completable

    @Insert
    fun addDataModel(vararg dataModel: DataModel)

    @Insert
    fun insertAll(dataModels: List<DataModel>): Completable

    @Query("DELETE from DataModel")
    fun deleteAll(): Completable
}