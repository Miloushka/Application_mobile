package com.example.suggestion

import com.example.suggestion.R

fun getCategoryAttributes(category: String): Pair<Int, Int> {
    return when (category) {
        "Loisir" -> Pair(R.color.loisirColor, R.drawable.ic_loisir)
        "Transport" -> Pair(R.color.transportColor, R.drawable.ic_transport)
        "Depense quotidienne" -> Pair(R.color.shoppingColor, R.drawable.ic_shopping)
        "Maison" -> Pair(R.color.homeColor, R.drawable.ic_home)
        else -> Pair(R.color.defaultColor, R.drawable.ic_add)
    }
}
