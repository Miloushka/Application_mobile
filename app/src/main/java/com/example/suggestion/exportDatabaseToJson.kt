package com.example.suggestion

import android.content.Context
import com.example.suggestion.data.DepenseDao
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

// Fonction principale pour exporter les données
fun exportDatabaseToJson(context: Context, depenseDao: DepenseDao) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // 1. Récupérer toutes les dépenses depuis la base de données
            val depenses = depenseDao.getAllDepenses()

            // 2. Convertir la liste de dépenses en JSON
            val jsonString = Gson().toJson(depenses)

            // 3. Écrire dans un fichier JSON
            val file = File(context.filesDir, "depenses.json")
            file.writeText(jsonString)

            println("Fichier JSON mis à jour : ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
