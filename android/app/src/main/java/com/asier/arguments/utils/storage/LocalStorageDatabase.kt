package com.asier.arguments.utils.storage

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update

@Database(entities = [StorageEntity::class], version = 1)
abstract class LocalStorageDatabase : RoomDatabase(){
    abstract fun getStorageDao() : LocalStorageDao
}
@Dao
abstract class LocalStorageDao{
    @Query("SELECT * FROM localStorage WHERE `key` = :key")
    abstract fun select(key : String) : StorageEntity
    @Insert
    abstract fun insert(entity : StorageEntity)
    @Delete
    abstract fun delete(entity: StorageEntity)
    @Update
    abstract fun update(entity: StorageEntity)
}
@Entity(tableName = "localStorage")
data class StorageEntity(
    @PrimaryKey
    val key : String,
    val value : String
)