// Cette classe utilitaire fournit des attributs associés aux catégories,
// comme une couleur et une icône, en fonction du nom de la catégorie spécifiée.

package com.example.suggestion

object CategoryUtils {
    fun getCategoryAttributes(category: String): Pair<Int, Int> {
        val normalizedCategory = category.lowercase().replace("é", "e").trim()
        return when (normalizedCategory) {
            "depense quotidienne" -> Pair(R.color.shoppingColor, R.drawable.ic_shopping)
            "transport" -> Pair(R.color.transportColor, R.drawable.ic_transport)
            "loisir" -> Pair(R.color.loisirColor, R.drawable.ic_loisir)
            "maison" -> Pair(R.color.homeColor, R.drawable.ic_home)
            else -> Pair(R.color.defaultColor, R.drawable.ic_add)
        }
    }
}
