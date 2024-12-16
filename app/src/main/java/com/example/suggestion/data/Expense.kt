package com.example.suggestion.data

//déclaration de la table expenses
//écrit par Jean-Guilhem

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


interface DisplayableItem {
    fun getTitle(): String
    fun getSubtitle(): String
    fun getDetails(): String
}

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

data class CategoryTotal(
    val categoryName: String,
    val totalAmount: Double,
): DisplayableItem {
    override fun getTitle(): String = categoryName
    override fun getSubtitle(): String = "$totalAmount€"
    override fun getDetails(): String = categoryName
}
