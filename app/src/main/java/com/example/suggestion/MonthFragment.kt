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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.List

class MonthFragment : Fragment() {

    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var selectedDateTextView: TextView
    private lateinit var totalRevenueTextView: TextView
    private lateinit var remainingBudgetTextView: TextView
    private lateinit var pieChart: PieChart
    private lateinit var recyclerView: RecyclerView

    private var allExpenses: List<Expense> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthSpinner = view.findViewById(R.id.spinner_month)
        yearSpinner = view.findViewById(R.id.spinner_year)
        selectedDateTextView = view.findViewById(R.id.received_date_text)
        totalRevenueTextView = view.findViewById(R.id.total_revenue_text)
        remainingBudgetTextView = view.findViewById(R.id.remaining_budget_text)
        pieChart = view.findViewById(R.id.pie_chart)
        recyclerView = view.findViewById(R.id.recycler_view_month)

        // Initialisation des Spinners pour le mois et l'année
        setupDateSpinners()

        // Charger toutes les dépenses depuis le JSON
        allExpenses = loadExpensesFromAssets()

        // Charger les dépenses initialement selon la date sélectionnée par défaut
        filterExpensesByDate()
    }

    // Configuration des spinners de date
    private fun setupDateSpinners() {
        val calendar = java.util.Calendar.getInstance()
        val currentYear = calendar.get(java.util.Calendar.YEAR)
        val currentMonth = calendar.get(java.util.Calendar.MONTH)

        // Mois et années
        val months = listOf(
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        )
        val years = (1900..currentYear).toList().reversed()

        monthSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        yearSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        monthSpinner.setSelection(currentMonth)
        yearSpinner.setSelection(years.indexOf(currentYear))

        // Listeners pour filtrer selon la date
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExpensesByDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExpensesByDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // Filtrer et mettre à jour les données selon la date
    private fun filterExpensesByDate() {
        val selectedMonth = monthSpinner.selectedItemPosition + 1
        val selectedYear = yearSpinner.selectedItem as Int

        selectedDateTextView.text = "Date sélectionnée : $selectedMonth/$selectedYear"

        // Utiliser le bon format "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)

        val filteredExpenses = allExpenses.filter {
            try {
                val date = dateFormat.parse(it.date)
                val calendar = java.util.Calendar.getInstance()
                calendar.time = date
                calendar.get(java.util.Calendar.MONTH) + 1 == selectedMonth &&
                        calendar.get(java.util.Calendar.YEAR) == selectedYear
            } catch (e: ParseException) {
                false // Ignorer les dates mal formatées
            }
        }

        // Consolider les dépenses par catégorie et mettre à jour l'interface
        updateUI(filteredExpenses)
    }


    // Mettre à jour l'interface utilisateur
    private fun updateUI(filteredExpenses: List<Expense>) {
        val totalRevenu = filteredExpenses.filter { it.category.equals("Revenu", ignoreCase = true) }.sumOf { it.price }
        val totalDepenses = filteredExpenses.filter { !it.category.equals("Revenu", ignoreCase = true) }.sumOf { it.price }
        val remainingBudget = totalRevenu - totalDepenses

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        totalRevenueTextView.text = "Total Revenu : ${currencyFormat.format(totalRevenu)}"
        remainingBudgetTextView.text = "Reste à dépenser : ${currencyFormat.format(remainingBudget)}"

        // Mettre à jour le PieChart
        pieChart.setData(filteredExpenses.map { CategoryTotal(it.category, it.price) })

        // Mettre à jour le RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(filteredExpenses, isMonthFragment = true, isAnnualView = false)
    }

    // Chargement des dépenses depuis les assets
    private fun loadExpensesFromAssets(): List<Expense> {
        val inputStream = context?.assets?.open("expenses.json")
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, object : TypeToken<List<Expense>>() {}.type)
    }
}
