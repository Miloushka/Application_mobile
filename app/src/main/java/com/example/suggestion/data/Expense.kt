package com.example.suggestion.data

//déclaration de la table expenses
//écrit par Jean-Guilhem

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Suppression en cascade si un utilisateur est supprimé
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int,
    val userId: Long, // Clé étrangère
    val amount: Double,
    var description: String,
    val date: String,
    val category: String
)

