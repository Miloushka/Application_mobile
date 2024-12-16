package com.example.suggestion

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suggestion.data.CategoryTotal
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.Expense
import com.example.suggestion.data.ExpenseViewModel
import com.example.suggestion.data.ExpenseViewModelFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthFragment : Fragment() {

    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var totalRevenueTextView: TextView
    private lateinit var remainingBudgetTextView: TextView
    private lateinit var pieChart: PieChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseViewModel: ExpenseViewModel
    private var expensesUserConnected: List<Expense> = emptyList() // Initialiser avec une liste vide

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser la base de données
        val database = DataBase.getDatabase(requireContext())
        val expanseDao = database.expenseDao()
        // Initialiser le ViewModel
        val factory = ExpenseViewModelFactory(expanseDao)
        expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        // Initialisation des vues
        monthSpinner = view.findViewById(R.id.spinner_month)
        yearSpinner = view.findViewById(R.id.spinner_year)
        totalRevenueTextView = view.findViewById(R.id.total_revenue_text)
        remainingBudgetTextView = view.findViewById(R.id.remaining_budget_text)
        pieChart = view.findViewById(R.id.pie_chart)
        recyclerView = view.findViewById(R.id.recycler_view_month)

        // Observer les dépenses via LiveData
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            expensesUserConnected = expenses.filter { it.userId == userConnected.userId }
            // Appliquer un filtre de mois/année ici si nécessaire
            filterExpensesByDate(expensesUserConnected)
        }

        // Initialisation des Spinners pour le mois et l'année
        setupDateSpinners()

        // Filtrer les dépenses initialement selon la date par défaut
        filterExpensesByDate(expensesUserConnected)

        // Listeners pour les Spinners pour mettre à jour les données en fonction de la sélection
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExpensesByDate(expensesUserConnected)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExpensesByDate(expensesUserConnected)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // Méthode pour filtrer les dépenses par date
    private fun filterExpensesByDate(expenses: List<Expense>) {
        val selectedMonth = monthSpinner.selectedItemPosition + 1 // Mois sélectionné (1-12)
        val selectedYear = yearSpinner.selectedItem as Int        // Année sélectionnée (ex: 2024)

        Log.d("MonthFragment", "Mois sélectionné: $selectedMonth, Année sélectionnée: $selectedYear")

        // Filtrer les dépenses par mois et année sélectionnés
        val filteredExpenses = expenses.filter { expense ->
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.FRANCE) // Format AAAAMMDD
            try {
                // Convertir la date (ex: "20241216") en objet Date
                val date = dateFormat.parse(expense.date)
                val calendar = Calendar.getInstance()
                calendar.time = date

                // Extraire le mois et l'année de la date
                val expenseMonth = calendar.get(Calendar.MONTH) + 1 // Les mois commencent à 0
                val expenseYear = calendar.get(Calendar.YEAR)

                // Comparaison avec le mois et l'année sélectionnés
                expenseMonth == selectedMonth && expenseYear == selectedYear
            } catch (e: Exception) {
                Log.e("MonthFragment", "Erreur lors du parsing de la date: ${expense.date}", e)
                false // Ignorer si la date est mal formatée
            }
        }

        Log.d("MonthFragment", "Dépenses filtrées: ${filteredExpenses.size}")

        // Consolider les dépenses par catégorie
        val consolidatedExpenses = consolidateExpenses(filteredExpenses)

        // Calcul du total des revenus et dépenses
        val totalRevenu = consolidatedExpenses
            .filter { it.category.equals("revenu", ignoreCase = true) }
            .sumOf { it.amount }

        val totalDepenses = consolidatedExpenses
            .filter { !it.category.equals("revenu", ignoreCase = true) }
            .sumOf { it.amount }

        val remainingBudget = totalRevenu - totalDepenses

        // Mettre à jour les vues TextView
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        totalRevenueTextView.text = "Total Revenu : ${currencyFormat.format(totalRevenu)}"
        remainingBudgetTextView.text = "Reste à dépenser : ${currencyFormat.format(remainingBudget)}"

        // Configuration du PieChart
        val centerTexts = listOf("Dépenses: ${currencyFormat.format(totalDepenses)}" to Color.RED)
        pieChart?.setData(consolidatedExpenses.map { CategoryTotal(it.category, it.amount) })
        pieChart?.setCenterTexts(centerTexts)

        // Mise à jour du RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(consolidatedExpenses, isMonthFragment = true, isAnnualView = false)

        // Afficher ou masquer le message "Aucune dépense trouvée"
        val noExpensesMessage: TextView = requireView().findViewById(R.id.no_expenses_message)
        if (filteredExpenses.isEmpty()) {
            val monthNames = listOf(
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
            )
            noExpensesMessage.text = "Aucune dépense trouvée pour ${monthNames[selectedMonth - 1]} $selectedYear."
            noExpensesMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noExpensesMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }




    // Méthode pour consolider les dépenses par catégorie
    private fun consolidateExpenses(expenses: List<Expense>): List<Expense> {
        val groupedExpenses = expenses.groupBy { it.category }

        return groupedExpenses.map { (category, categoryExpenses) ->
            val totalPrice = categoryExpenses.sumOf { it.amount }
            val descriptionWithPricesStr = categoryExpenses.joinToString(separator = "\n") {
                "${it.description} - ${it.amount}€"
            }

            Expense(
                userId = userConnected.userId,
                expenseId = category.hashCode(),
                category = category,
                amount = totalPrice,
                description = descriptionWithPricesStr,
                date = "" // ou une date spécifique si nécessaire
            )
        }
    }

    // Configuration des spinners pour le mois et l'année
    private fun setupDateSpinners() {
        val calendar = java.util.Calendar.getInstance()
        val currentYear = calendar.get(java.util.Calendar.YEAR)
        val currentMonth = calendar.get(java.util.Calendar.MONTH)

        val months = listOf(
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        )
        val years = (1900..currentYear).toList().reversed()

        monthSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        yearSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        monthSpinner.setSelection(currentMonth)
        yearSpinner.setSelection(years.indexOf(currentYear))
    }
}
