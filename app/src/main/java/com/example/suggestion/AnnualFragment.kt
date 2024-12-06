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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class AnnualFragment : Fragment() {
//TODO("relier a la base de donnée")
    private lateinit var yearSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_annual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation du Spinner pour l'année
        yearSpinner = view.findViewById(R.id.spinner_year)
        setupYearSpinner()

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
        recyclerView.adapter = ExpenseAdapter(consolidatedExpenses, isAnnualView = true, isMonthFragment = false, requireContext())

        // Ajouter un listener pour le Spinner pour filtrer les dépenses selon l'année
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Filtrer les dépenses en fonction de l'année sélectionnée
                val selectedYear = yearSpinner.selectedItem as Int
                val filteredExpenses = expenses.filter { expense ->
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
                    val categoryTotals = filteredConsolidatedExpenses.map { CategoryTotal(it.category, it.price) }
                    it.setData(categoryTotals)
                }

                // Mettre à jour le RecyclerView avec les dépenses filtrées
                recyclerView.adapter = ExpenseAdapter(filteredConsolidatedExpenses, isAnnualView = true, isMonthFragment = false, requireContext())

                // Affichage dynamique du message "Aucune dépense trouvée"
                val noExpensesMessage: TextView = requireView().findViewById(R.id.no_expenses_message)
                val yearMessage = getString(R.string.no_expenses_message, selectedYear)

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

    // Fonction pour charger les dépenses à partir du fichier JSON dans assets
    private fun loadExpensesFromAssets(): List<ExpenseApp> {
        val assetManager = context?.assets
        val inputStream = assetManager?.open("expenses.json") // Ouvrir le fichier JSON dans assets
        val reader = InputStreamReader(inputStream)
        val gson = Gson()

        // Utilisation de Gson pour parser le JSON
        val expenseListType = object : TypeToken<List<ExpenseApp>>() {}.type
        return gson.fromJson(reader, expenseListType)
    }

    // Méthode pour consolider les dépenses par catégorie
    private fun consolidateExpenses(expenses: List<ExpenseApp>): List<ExpenseApp> {
        val groupedExpenses = expenses.groupBy { it.category }

        return groupedExpenses.map { (category, categoryExpenses) ->
            // Calcule la somme des dépenses pour chaque catégorie
            val totalPrice = categoryExpenses.sumOf { it.price }

            // Concatène les descriptions des dépenses dans chaque catégorie
            val concatenatedDescriptions = categoryExpenses.joinToString(separator = "\n") { it.description }

            TODO("Modifier le user ID")
            // Crée une nouvelle dépense consolidée pour chaque catégorie
            ExpenseApp(
                userId = 1,
                expenseId = category.hashCode(), // Génère un ID unique basé sur la catégorie
                category = category,
                price = totalPrice,
                description = concatenatedDescriptions,
                date = ""  // La date peut être vide ou ajoutée si nécessaire
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
