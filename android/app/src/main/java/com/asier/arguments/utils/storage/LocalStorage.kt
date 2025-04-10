package com.asier.arguments.utils.storage

import android.content.Context
import androidx.room.Room

class LocalStorage(context: Context) {
    private val db = Room.databaseBuilder(context, LocalStorageDatabase::class.java,"local_storage")
        .allowMainThreadQueries()
        .build()
        .getStorageDao()

    fun save(key : String, value: String){
        db.insert(StorageEntity(key,value))
    }

    fun load(key: String) : String {
       return db.select(key).value
    }

    fun delete(key: String){
        db.delete(StorageEntity(key,""))
    }
}