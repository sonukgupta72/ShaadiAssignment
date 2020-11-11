package com.sonukgupta72.shaadiassignment.db

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.sonukgupta72.shaadiassignment.MyApplication
import io.reactivex.Flowable
import io.reactivex.Completable
import io.reactivex.Single


class RepositoryManager {

    companion object {
        var myDataBase: MyDataBase? = null

        fun getRepositoryManager() : RepositoryManager {
            MyApplication.appContext?.run {
                myDataBase = getDataBase(this)
            }
            return RepositoryManager()
        }

        private fun getDataBase(context: Context): MyDataBase {
            return if (myDataBase != null) {
                myDataBase as MyDataBase
            } else{
                myDataBase = Room.databaseBuilder(context, MyDataBase::class.java, "my-table.db")
                    .fallbackToDestructiveMigration().build()
                myDataBase as MyDataBase
            }
        }
    }

    fun getLiveData(): LiveData<List<DataModel>>? {
        return myDataBase?.getDataDao()?.getLiveDataModel()
    }

    fun getAllData(): Single<List<DataModel>>? {
        return myDataBase?.getDataDao()?.getAllDataModel()
    }

    fun deleteAll(): Completable? {
        return myDataBase?.getDataDao()?.deleteAll()
    }

    fun updateDateModel(dataModel: DataModel, responseMessage: String): Completable? {
        return myDataBase?.getDataDao()?.updateDateModel(dataModel.profileInfo, responseMessage, dataModel.id)
    }

    fun insertAll(list: List<DataModel>): Completable? {
        return myDataBase?.getDataDao()?.insertAll(list)
    }
}