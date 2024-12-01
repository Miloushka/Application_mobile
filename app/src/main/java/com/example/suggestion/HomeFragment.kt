package com.example.suggestion

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private val initialRevenue = 1200.0 // Revenu initial de la personne

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bouton pour ouvrir AddCategorieActivity
        val addCategorieButton: ImageButton = view.findViewById(R.id.button_open_add_categorie)
        addCategorieButton.setOnClickListener {
            val dialog = AddCategorieDialogFragment()
            dialog.show(parentFragmentManager, "AddCategorieDialogFragment")
        }

        // Charger les données depuis le fichier JSON
        val expenses = loadExpensesFromJson(requireContext(), "expenses.json")

        // Agréger les dépenses par catégorie
        val aggregatedExpenses = aggregateExpensesByCategory(expenses)

        // RecyclerView pour afficher les dépenses
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_expenses)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Définir l'adaptateur et gérer les clics
        val adapter = ExpenseAdapter(expenses, isAnnualView = false, isMonthFragment = false)
        adapter.setOnExpenseClickListener { expense ->
            // Lorsque l'utilisateur clique sur une dépense, ouvrez le fragment d'édition
            val fragment = EditExpenseFragment()
            val bundle = Bundle()
            bundle.putParcelable("expense", expense)
            fragment.arguments = bundle

            // Remplacer le fragment actuel par celui d'édition
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Remplacer avec le conteneur approprié
                .addToBackStack(null) // Ajouter à la pile arrière pour pouvoir revenir en arrière
                .commit()
        }

        recyclerView.adapter = adapter
    }

    // Fonction pour agréger les dépenses par catégorie
    private fun aggregateExpensesByCategory(expenses: List<Expense>): List<CategoryTotal> {
        return expenses
            .groupBy { it.category }
            .map { (category, expenseList) ->
                val totalAmount = expenseList.sumOf { it.price }
                CategoryTotal(category, totalAmount)
            }
    }

    // Fonction pour charger les dépenses depuis un fichier JSON dans assets
    private fun loadExpensesFromJson(context: Context, fileName: String): List<Expense> {
        val inputStream = context.assets.open(fileName)
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val expensesType = object : TypeToken<List<Expense>>() {}.type
        return gson.fromJson(reader, expensesType)
    }
}

