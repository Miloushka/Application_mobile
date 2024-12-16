package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suggestion.data.CategoryTotal
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.Expense
import com.example.suggestion.data.ExpenseViewModel
import com.example.suggestion.data.ExpenseViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class AnnualFragment : Fragment() {

    private lateinit var yearSpinner: Spinner
    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_annual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser la base de données
        val database = DataBase.getDatabase(requireContext())
        val expanseDao = database.expenseDao()
        // Initialiser le ViewModel
        val factory = ExpenseViewModelFactory(expanseDao)
        expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]



        // Initialisation du Spinner pour l'année
        yearSpinner = view.findViewById(R.id.spinner_year)
        setupYearSpinner()

        // Récuperer les dépenses depuis la base de donnée
        expenseViewModel.getExpenses(userConnected.userId)

        // Consolider les dépenses par catégorie
        val consolidatedExpenses = consolidateExpenses(expensesUserConnected)

        // Initialiser le PieChart et afficher les données consolidées
        val pieChart: PieChart = view.findViewById(R.id.pie_chart)
        pieChart?.let {
            val categoryTotals = consolidatedExpenses.map { CategoryTotal(it.category, it.amount) }
            it.setData(categoryTotals)
        }

        // Initialiser le RecyclerView et l'adaptateur
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_annual)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(consolidatedExpenses, isAnnualView = true, isMonthFragment = false)

        // Ajouter un listener pour le Spinner pour filtrer les dépenses selon l'année
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Filtrer les dépenses en fonction de l'année sélectionnée
                val selectedYear = yearSpinner.selectedItem as Int
                val filteredExpenses = expensesUserConnected.filter { expense ->
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
                    try {
                        val date = dateFormat.parse(expense.date)
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        calendar.get(Calendar.YEAR) == selectedYear
                    } catch (e: Exception) {
                        false // Ignorer les dates mal formatées
                    }
                }

                // Consolider les dépenses filtrées par catégorie
                val filteredConsolidatedExpenses = consolidateExpenses(filteredExpenses)

                // Mettre à jour le PieChart avec les données filtrées
                pieChart?.let {
                    val categoryTotals = filteredConsolidatedExpenses.map { CategoryTotal(it.category, it.amount) }
                    it.setData(categoryTotals)
                }

                // Mettre à jour le RecyclerView avec les dépenses filtrées
                recyclerView.adapter = ExpenseAdapter(filteredConsolidatedExpenses, isAnnualView = true, isMonthFragment = false)

                // Mettre à jour le RecyclerView avec les dépenses filtrées
                recyclerView.adapter = ExpenseAdapter(filteredConsolidatedExpenses, isAnnualView = true, isMonthFragment = false)

                // Affichage dynamique du message "Aucune dépense trouvée"
                val noExpensesMessage: TextView = requireView().findViewById(R.id.no_expenses_message)
                val yearMessage = "Aucune dépense trouvée pour $selectedYear."

                if (filteredExpenses.isEmpty()) {
                    // Si aucune dépense n'est trouvée, afficher le message
                    noExpensesMessage.text = yearMessage  // Mettre à jour le texte avec l'année sélectionnée
                    noExpensesMessage.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    // Si des dépenses sont trouvées, masquer le message et afficher le RecyclerView
                    noExpensesMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }



    // Méthode pour regrouper les dépenses par catégorie
    private fun consolidateExpenses(expenses: List<Expense>): List<Expense> {
        // Grouper les dépenses par catégorie
        val groupedExpenses = expenses.groupBy { it.category }

        // Transformer chaque groupe en une dépense consolidée
        return groupedExpenses.map { (category, categoryExpenses) ->
            // Calcule la somme des montants pour la catégorie
            val totalPrice = categoryExpenses.sumOf { it.amount }

            // Concatène les descriptions des dépenses pour cette catégorie
            val concatenatedDescriptions = categoryExpenses.joinToString(separator = "\n") { it.description }

            // Crée une dépense consolidée pour cette catégorie
            Expense(
                userId = userConnected.userId, // Utilisateur connecté
                expenseId = category.hashCode(), // ID unique basé sur la catégorie
                category = category, // Catégorie
                amount = totalPrice, // Total des montants consolidés
                description = concatenatedDescriptions, // Descriptions concaténées
                date = "" // Date par défaut ou à ajuster
            )
        }
    }


    // Configuration du Spinner pour l'année
    private fun setupYearSpinner() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        // Liste des années disponibles (de 1900 à l'année actuelle)
        val years = (1900..currentYear).toList().reversed()

        // Configuration de l'adaptateur pour le Spinner
        yearSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        // Sélection par défaut : année actuelle
        yearSpinner.setSelection(years.indexOf(currentYear))
    }
}
