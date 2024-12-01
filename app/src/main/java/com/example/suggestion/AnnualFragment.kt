package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class AnnualFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_annual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePickerFragment = DatePickerFragment.newInstance(false)
        childFragmentManager.beginTransaction()
            .replace(R.id.date_picker_container, datePickerFragment)
            .commit()


        // Charger et parser les dépenses depuis le fichier JSON dans assets
        val expenses = loadExpensesFromAssets()

        // Consolider les dépenses par catégorie
        val consolidatedExpenses = consolidateExpenses(expenses)

        // Initialiser le PieChart et afficher les données consolidées
        val pieChart: PieChart = view.findViewById(R.id.pie_chart)
        pieChart?.let {
            val categoryTotals = consolidatedExpenses.map { CategoryTotal(it.category, it.price) }
            it.setData(categoryTotals)
        }


        // Initialiser le RecyclerView et l'adaptateur
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_annual)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(consolidatedExpenses, isAnnualView = true, isMonthFragment = false)
    }

    // Fonction pour charger les dépenses à partir du fichier JSON dans assets
    private fun loadExpensesFromAssets(): List<Expense> {
        val assetManager = context?.assets
        val inputStream = assetManager?.open("expenses.json") // Ouvrir le fichier JSON dans assets
        val reader = InputStreamReader(inputStream)
        val gson = Gson()

        // Utilise Gson pour parser le JSON
        val expenseListType = object : TypeToken<List<Expense>>() {}.type
        return gson.fromJson(reader, expenseListType)
    }

    // Méthode pour consolider les dépenses par catégorie
    private fun consolidateExpenses(expenses: List<Expense>): List<Expense> {
        val groupedExpenses = expenses.groupBy { it.category }

        return groupedExpenses.map { (category, categoryExpenses) ->
            // Calcule la somme des dépenses pour chaque catégorie
            val totalPrice = categoryExpenses.sumOf { it.price }

            // Concatène les descriptions des dépenses dans chaque catégorie
            val concatenatedDescriptions = categoryExpenses.joinToString(separator = "\n") { it.description }

            // Crée une nouvelle dépense consolidée pour chaque catégorie
            Expense(
                id = category.hashCode(), // Génère un ID unique basé sur la catégorie
                category = category,
                price = totalPrice,
                description = concatenatedDescriptions,
                date = ""  // La date peut être vide ou ajoutée si nécessaire
            ).apply {
                // Ajoute les prix détaillés sous forme de chaîne
                val descriptionWithPrices = categoryExpenses.map { "${it.price}€" }
                this.detailPrices = descriptionWithPrices.joinToString(separator = "\n")
            }
        }
    }
}
