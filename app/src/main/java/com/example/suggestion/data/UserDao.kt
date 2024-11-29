package com.example.suggestion.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//Contient toutes les methodes pour manipuler la base de donnée
//écrit par Jean-Guilhem

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query(value = "SELECT * FROM users ORDER BY userId ASC")
    fun readAllData(): LiveData<List<User>>
}