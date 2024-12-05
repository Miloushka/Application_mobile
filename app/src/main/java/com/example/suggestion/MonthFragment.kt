package com.example.suggestion

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class MonthFragment : Fragment() {

    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var totalRevenueTextView: TextView
    private lateinit var remainingBudgetTextView: TextView
    private lateinit var pieChart: PieChart
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation des vues
        monthSpinner = view.findViewById(R.id.spinner_month)
        yearSpinner = view.findViewById(R.id.spinner_year)
        totalRevenueTextView = view.findViewById(R.id.total_revenue_text)
        remainingBudgetTextView = view.findViewById(R.id.remaining_budget_text)
        pieChart = view.findViewById(R.id.pie_chart)
        recyclerView = view.findViewById(R.id.recycler_view_month)

        // Charger et parser les dépenses depuis le fichier JSON dans assets
        val expenses = loadExpensesFromAssets()

        // Initialisation des Spinners pour le mois et l'année
        setupDateSpinners()

        // Filtrer les dépenses initialement selon la date par défaut
        filterExpensesByDate(expenses)

        // Listeners pour les Spinners pour mettre à jour les données en fonction de la sélection
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExpensesByDate(expenses)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExpensesByDate(expenses)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

    // Méthode pour filtrer les dépenses par date
    private fun filterExpensesByDate(expenses: List<Expense>) {
        val selectedMonth = monthSpinner.selectedItemPosition + 1
        val selectedYear = yearSpinner.selectedItem as Int

        // Filtrer les dépenses par mois et année sélectionnés
        val filteredExpenses = expenses.filter {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
            try {
                val date = dateFormat.parse(it.date)
                val calendar = java.util.Calendar.getInstance()
                calendar.time = date
                calendar.get(java.util.Calendar.MONTH) + 1 == selectedMonth &&
                        calendar.get(java.util.Calendar.YEAR) == selectedYear
            } catch (e: Exception) {
                false // Ignorer les dates mal formatées
            }
        }

        // Consolider les dépenses par catégorie
        val consolidatedExpenses = consolidateExpenses(filteredExpenses)

        // Calcul du total des revenus et du reste à dépenser
        val totalRevenu = consolidatedExpenses.filter { it.category.equals("revenu", ignoreCase = true) }
            .sumOf { it.price }

        val totalDepenses = consolidatedExpenses.filter { !it.category.equals("revenu", ignoreCase = true) }
            .sumOf { it.price }

        val remainingBudget = totalRevenu - totalDepenses

        // Mettre à jour les TextViews dans la CardView
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        totalRevenueTextView.text = "Total Revenu : ${currencyFormat.format(totalRevenu)}"
        remainingBudgetTextView.text = "Reste à dépenser : ${currencyFormat.format(remainingBudget)}"

        // Préparer les textes à afficher au centre du PieChart
        val centerTexts = listOf(
            "Dépenses: ${currencyFormat.format(totalDepenses)}" to Color.RED
        )

        // Configuration du PieChart
        pieChart?.let {
            val categoryTotals = consolidatedExpenses.map { CategoryTotal(it.category, it.price) }
            it.setData(categoryTotals)
            it.setCenterTexts(centerTexts) // Passer les textes au centre du PieChart
        }

        // Initialiser le RecyclerView et l'adaptateur
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(consolidatedExpenses, isMonthFragment = true, isAnnualView = false)
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

    // Configuration des spinners pour le mois et l'année
    private fun setupDateSpinners() {
        val calendar = java.util.Calendar.getInstance()
        val currentYear = calendar.get(java.util.Calendar.YEAR)
        val currentMonth = calendar.get(java.util.Calendar.MONTH)

        // Liste des mois et années
        val months = listOf(
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        )
        val years = (1900..currentYear).toList().reversed()

        // Configuration des adaptateurs pour les Spinners
        monthSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        yearSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        // Sélection par défaut : mois et année actuels
        monthSpinner.setSelection(currentMonth)
        yearSpinner.setSelection(years.indexOf(currentYear))
    }
}
