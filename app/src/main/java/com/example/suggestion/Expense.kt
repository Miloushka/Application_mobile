// Ce fichier définit l'interface `DisplayableItem` pour uniformiser l'affichage des éléments
// avec un titre, un sous-titre et des détails. Les classes `Expense` et `CategoryTotal` implémentent
// cette interface pour représenter respectivement une dépense individuelle et le total d'une catégorie.

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
    var detailPrices: String? = null
    override fun getTitle(): String = category
    override fun getSubtitle(): String = "$price€"
    override fun getDetails(): String = description
}

data class CategoryTotal(
    val categoryName: String,
    val totalAmount: Double,
) : DisplayableItem {
    override fun getTitle(): String = categoryName
    override fun getSubtitle(): String = "$totalAmount€"
    override fun getDetails(): String = categoryName
}
