package com.example.suggestion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_table")  // Vérifiez que le nom de la table est bien "my_table"
data class MyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
