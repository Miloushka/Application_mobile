package com.example.suggestion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyDao {

    @Insert
    suspend fun insert(entity: MyEntity)

    @Query("SELECT * FROM my_table")  // Assurez-vous que le nom de la table est bien "my_table"
    suspend fun getAll(): List<MyEntity>
}
