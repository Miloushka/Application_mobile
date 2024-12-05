package com.example.suggestion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepenseDao {
    @Insert
    suspend fun insert(depense: Depense)

    @Insert
    suspend fun insertAll(depenses: List<Depense>)  // Insertion en bloc

    @Query("SELECT * FROM depense")
    suspend fun getAllDepenses(): List<Depense>
}

