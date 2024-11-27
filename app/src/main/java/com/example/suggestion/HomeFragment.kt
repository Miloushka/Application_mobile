// Ce fragment affiche l'écran principal de l'application avec une liste de dépenses.
// Il permet également d'ouvrir un dialogue pour ajouter une nouvelle catégorie de dépense.
// Les dépenses sont agrégées par catégorie et affichées sous forme de graphique circulaire
// et d'une liste recyclée. Le fragment gère l'ajout de catégories via un dialogue et la mise
// à jour de la vue en fonction des données des dépenses.

package com.example.suggestion

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    private val initialRevenue = 1200.0 // Revenu initial de la personne

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Gonfler le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bouton pour ouvrir AddCategorieActivity
        val addCategorieButton: ImageButton = view.findViewById(R.id.button_open_add_categorie)
        addCategorieButton.setOnClickListener {
            val dialog = AddCategorieDialogFragment()
            dialog.show(requireFragmentManager(), "AddCategorieDialogFragment")
        }

        // Liste d'exemples de dépenses
        val expenses = listOf(
            Expense("Depense quotidienne", 12.50, "Lunch at a restaurant", 1699954800000),
            Expense("Transport", 3.00, "Bus ticket", 1699958400000),
            Expense("Loisir", 45.00, "New shoes", 1699962000000),
            Expense("Maison", 15.00, "Movie night", 1699965600000),
            Expense("Depense quotidienne", 12.50, "Lunch at a restaurant", 1699954800000),
            Expense("Transport", 3.00, "Bus ticket", 1699958400000),
            Expense("Loisir", 45.00, "New shoes", 1699962000000),
            Expense("Maison", 15.00, "Movie night", 1699965600000),
            Expense("Depense quotidienne", 12.50, "Lunch at a restaurant", 1699954800000),
            Expense("Transport", 3.00, "Bus ticket", 1699958400000),
            Expense("Loisir", 45.00, "New shoes", 1699962000000),
            Expense("Maison", 15.00, "Movie night", 1699965600000)
        )

        val aggregatedExpenses = aggregateExpensesByCategory(expenses)
        val pieChart: PieChart = view.findViewById(R.id.pie_chart)
        pieChart.setData(aggregatedExpenses)

        // Calcul de la somme des dépenses et du revenu restant
        val totalExpenses = expenses.sumOf { it.price }
        val remainingRevenue = initialRevenue - totalExpenses

        // Ajouter les textes au centre du diagramme
        pieChart.setCenterTexts(
            listOf(
                Pair("${totalExpenses} €", Color.RED),       // Dépenses en rouge
                Pair("${remainingRevenue} €", Color.GREEN)  // Revenu restant en vert
            )
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_expenses)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(expenses, isAnnualView = false, isMonthFragment= false)



    }

    private fun aggregateExpensesByCategory(expenses: List<Expense>): List<CategoryTotal> {
        return expenses
            .groupBy { it.category }
            .map { (category, expenseList) ->
                val totalAmount = expenseList.sumOf { it.price }
                CategoryTotal(category, totalAmount)
            }
    }

}
