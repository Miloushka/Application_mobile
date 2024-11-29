package com.example.suggestion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Class représentant la table Users de la base de données
//écrit par Jean-Guilhem

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val lastName: String,
    val firstName: String,
    val dateOfBirth: String,
    val email: String,
    val password: String
)