package com.sonukgupta72.shaadiassignment.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataModel::class], version = 2)
@TypeConverters(Converters::class)
abstract class MyDataBase : RoomDatabase() {
    abstract fun getDataDao(): MyDataDao
}