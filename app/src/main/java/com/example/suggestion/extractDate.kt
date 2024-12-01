package com.example.suggestion
import java.text.SimpleDateFormat
import java.util.*

fun extractDate(dateString: String): Pair<Int, Int> {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = format.parse(dateString)
    return if (date != null) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1  // Mois de 1 à 12
        Pair(year, month)
    } else {
        Pair(-1, -1)  // Retourne une valeur par défaut si la date est invalide
    }
}
