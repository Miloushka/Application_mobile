package com.example.suggestion

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditExpenseFragment : Fragment() {

    private var expense: Expense? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupérer la dépense à modifier
        expense = arguments?.getParcelable("expense")

        // Remplir les champs avec les valeurs de la dépense actuelle
        expense?.let {
            val categoryEditText: EditText = view.findViewById(R.id.category_edit_text)
            val descriptionEditText: EditText = view.findViewById(R.id.description_edit_text)
            val priceEditText: EditText = view.findViewById(R.id.price_edit_text)
            val dateEditText: EditText = view.findViewById(R.id.date_edit_text)

            categoryEditText.setText(it.category)
            descriptionEditText.setText(it.description)
            priceEditText.setText(it.price.toString())

            // Parse de la date en utilisant un SimpleDateFormat
            val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
            try {
                val parsedDate = dateFormat.parse(it.date)
                // Si vous avez besoin de manipuler la date, vous pouvez utiliser `parsedDate`
                dateEditText.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsedDate))
            } catch (e: ParseException) {
                e.printStackTrace()
                // Si la date n'est pas valide, vous pouvez gérer l'erreur ici, ou laisser vide
                dateEditText.setText("")
            }
        }

        // Sauvegarder les modifications
        val saveButton: Button = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val updatedCategory = view.findViewById<EditText>(R.id.category_edit_text).text.toString()
            val updatedDescription = view.findViewById<EditText>(R.id.description_edit_text).text.toString()
            val updatedPrice = view.findViewById<EditText>(R.id.price_edit_text).text.toString().toDouble()
            val updatedDate = view.findViewById<EditText>(R.id.date_edit_text).text.toString()

            expense?.apply {
                category = updatedCategory
                description = updatedDescription
                price = updatedPrice
                date = updatedDate

                // Mettre à jour la dépense dans la liste globale et sauvegarder dans le fichier JSON
                updateExpenseInList(this)
            }

            // Fermer le fragment
            parentFragmentManager.popBackStack()
        }
    }

    // Fonction pour mettre à jour la dépense dans la liste et sauvegarder dans les fichiers
    private fun updateExpenseInList(updatedExpense: Expense) {
        // Charger la liste des dépenses actuelles depuis le fichier
        val expenses = loadExpensesFromFile().toMutableList()

        // Mettre à jour la dépense dans la liste
        val index = expenses.indexOfFirst { it.id == updatedExpense.id }
        if (index != -1) {
            expenses[index] = updatedExpense
        }

        // Sauvegarder à nouveau le fichier JSON avec les dépenses mises à jour
        saveExpensesToFile(requireContext(), expenses)
    }

    // Fonction pour charger les dépenses depuis le fichier (fonction que vous devez avoir)
    private fun loadExpensesFromFile(): List<Expense> {
        // Vous pouvez implémenter la fonction pour charger les dépenses depuis le fichier ici,
        // cela peut être une autre méthode que vous avez déjà, ou réutiliser la méthode loadExpensesFromJson.
        return emptyList()  // Exemple vide
    }

    // Fonction pour sauvegarder la liste mise à jour dans le fichier JSON
    private fun saveExpensesToFile(context: Context, expenses: List<Expense>) {
        val json = Gson().toJson(expenses)
        try {
            // Ouvrir un fichier en mode privé pour écrire les données JSON
            val fileOutputStream = context.openFileOutput("expenses.json", Context.MODE_PRIVATE)

            // Convertir la liste en JSON et l'écrire dans le fichier
            fileOutputStream.write(json.toByteArray())
            fileOutputStream.close()  // Fermer le fichier après écriture

        } catch (e: Exception) {
            // Afficher une erreur si l'écriture échoue
            e.printStackTrace()
        }
    }
}
