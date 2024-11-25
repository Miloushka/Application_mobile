package com.example.suggestion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<Expense>
}
