// Cette classe utilitaire fournit des attributs associés aux catégories,
// comme une couleur et une icône, en fonction du nom de la catégorie spécifiée.

package com.example.suggestion

import android.content.Context

object CategoryUtils {
    fun getCategoryAttributes(context: Context, category: String): Pair<Int, Int> {
        val normalizedCategory = category.lowercase().replace("é", "e")?.trim()
        return when (normalizedCategory) {
            context.getString(R.string.category_daily_expense).lowercase() -> Pair(R.color.shoppingColor, R.drawable.ic_shopping)
            context.getString(R.string.category_transport).lowercase() -> Pair(R.color.transportColor, R.drawable.ic_transport)
            context.getString(R.string.category_leisure).lowercase() -> Pair(R.color.loisirColor, R.drawable.ic_loisir)
            context.getString(R.string.category_home).lowercase() -> Pair(R.color.homeColor, R.drawable.ic_home)
            context.getString(R.string.category_income).lowercase() -> Pair(R.color.incomeColor, R.drawable.ic_income)
            else -> Pair(R.color.defaultColor, R.drawable.ic_add)
        }
    }
}
