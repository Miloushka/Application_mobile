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
    suspend fun addUser(user: User): Long

    @Query(value = "SELECT * FROM users ORDER BY userId ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): User?

    @Query("UPDATE users SET password = :newPassword WHERE email = :email")
    suspend fun changePassword(email: String, newPassword: String)
}