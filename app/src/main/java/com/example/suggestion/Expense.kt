package com.example.suggestion

interface DisplayableItem {
    fun getTitle(): String
    fun getSubtitle(): String
    fun getDetails(): String
}

data class Expense(
    val category: String,
    val price: Double,
    val description: String,
    val date: Long
) : DisplayableItem {
    override fun getTitle(): String = category
    override fun getSubtitle(): String = "$price€"
    override fun getDetails(): String = description
}

data class CategoryTotal(
    val categoryName: String,
    val totalAmount: Double
) : DisplayableItem {
    override fun getTitle(): String = categoryName
    override fun getSubtitle(): String = "Total: $totalAmount€"
    override fun getDetails(): String = categoryName
}
