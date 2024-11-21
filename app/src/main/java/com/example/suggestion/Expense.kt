package com.example.suggestion

interface DisplayableItem {
    fun getTitle(): String // Le titre, comme la catégorie ou la description
    fun getSubtitle(): String // Un sous-titre, comme le montant total ou une description
}

data class Expense(
    val category: String,
    val price: Double,
    val description: String,
    val date: Long
) : DisplayableItem {
    override fun getTitle(): String = category
    override fun getSubtitle(): String = "$price€ - $description"
}

data class CategoryTotal(
    val categoryName: String,
    val totalAmount: Double
) : DisplayableItem {
    override fun getTitle(): String = categoryName
    override fun getSubtitle(): String = "Total: $totalAmount€"
}
