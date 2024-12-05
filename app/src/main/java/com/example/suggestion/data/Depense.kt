package com.example.suggestion.data  // Cette classe se trouve dans le package des données

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "depense")  // L'annotation @Entity marque cette classe comme une entité Room
data class Depense(
    @PrimaryKey val id: Int,  // Déclare une clé primaire unique
    val category: String,
    val price: Double,
    val description: String,
    val date: String
)
