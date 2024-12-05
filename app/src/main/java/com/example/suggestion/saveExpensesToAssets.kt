package com.example.suggestion

import android.content.Context
import com.google.gson.Gson
import java.io.FileOutputStream

// Fonction pour sauvegarder dans un fichier JSON dans le stockage interne
fun saveExpensesToFile(context: Context, expenses: List<Expense>) {
    val json = Gson().toJson(expenses)

    try {
        // Ouvrir un fichier en mode privé pour écrire les données JSON
        val fileOutputStream: FileOutputStream = context.openFileOutput("expenses.json", Context.MODE_PRIVATE)

        // Convertir la liste en JSON et l'écrire dans le fichier
        fileOutputStream.write(json.toByteArray())
        fileOutputStream.close()  // Fermer le fichier après écriture

    } catch (e: Exception) {
        // Afficher une erreur si l'écriture échoue
        e.printStackTrace()
    }
}
