import android.content.Context
import android.util.Log
import com.example.suggestion.Expense
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader

fun loadExpensesFromJson(context: Context, fileName: String): List<Expense> {
    val expenses = mutableListOf<Expense>()
    try {
        // Vérification du chemin
        Log.d("HomeFragment", "Tentative d'ouverture du fichier $fileName dans les assets.")

        // Accéder aux assets
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName) // Ouvrir le fichier JSON

        // Lire le fichier en tant que chaîne de caractères
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Parse le fichier JSON
        val gson = Gson()
        val expenseListType = object : TypeToken<List<Expense>>() {}.type
        expenses.addAll(gson.fromJson(jsonString, expenseListType)) // Convertir la chaîne en liste d'objets Expense

        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e("HomeFragment", "Erreur lors de l'ouverture du fichier : ${e.message}")
    }

    return expenses
}
