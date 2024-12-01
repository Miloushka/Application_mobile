package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.text.NumberFormat
import java.util.Locale

class MonthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePickerFragment = DatePickerFragment.newInstance(true)
        childFragmentManager.beginTransaction()
            .replace(R.id.date_picker_container, datePickerFragment)
            .commit()

        // Charger et parser les dépenses depuis le fichier JSON dans assets
        val expenses = loadExpensesFromAssets()

        // Consolider les dépenses par catégorie
        val consolidatedExpenses = consolidateExpenses(expenses)

        // Calcul du total des revenus et du reste à dépenser
        val totalRevenu = consolidatedExpenses.filter { it.category.equals("revenu", ignoreCase = true) }
            .sumOf { it.price }

        val totalDepenses = consolidatedExpenses.filter { !it.category.equals("revenu", ignoreCase = true) }
            .sumOf { it.price }

        val remainingBudget = totalRevenu - totalDepenses

        // Mettre à jour les TextViews dans la CardView
        val totalRevenueTextView: TextView = view.findViewById(R.id.total_revenue_text)
        val remainingBudgetTextView: TextView = view.findViewById(R.id.remaining_budget_text)

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        totalRevenueTextView.text = "Total Revenu : ${currencyFormat.format(totalRevenu)}"
        remainingBudgetTextView.text = "Reste à dépenser : ${currencyFormat.format(remainingBudget)}"

        // Initialiser le PieChart et afficher les données consolidées
        val pieChart: PieChart = view.findViewById(R.id.pie_chart)
        pieChart?.let {
            val categoryTotals = consolidatedExpenses.map { CategoryTotal(it.category, it.price) }
            it.setData(categoryTotals)
        }

        // Initialiser le RecyclerView et l'adaptateur
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_month)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(consolidatedExpenses, isMonthFragment = true, isAnnualView = false)
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
            val totalPrice = categoryExpenses.sumOf { it.price }
            val concatenatedDescriptions = categoryExpenses.joinToString(separator = "\n") { it.description }
            val descriptionWithPrices = categoryExpenses.map { "${it.price}€" }
            val descriptionWithPricesStr = descriptionWithPrices.joinToString(separator = "\n")

            // Utilisation d'un ID généré pour chaque dépense consolidée
            Expense(
                id = category.hashCode(), // Génère un ID unique basé sur la catégorie
                category = category,
                price = totalPrice,
                description = concatenatedDescriptions,
                date = ""  // La date peut être vide ou ajoutée si nécessaire
            ).apply {
                this.detailPrices = descriptionWithPricesStr // Ajouter les prix détaillés sous forme de chaîne
            }
        }
    }
}
