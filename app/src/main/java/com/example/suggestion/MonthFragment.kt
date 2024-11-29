//Fragment représentant la vue des dépenses mensuelles
//Affichage regroupé par catégorie, avec le prix total, l'affichage de la card comporte
//egalement le description de chaque dépense avec son prix associé
package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        // Exemple de données
        val expenses = listOf(
            Expense("Depense quotidienne", 13.30, "Lunch at a restaurant lundi", 1699954800000),
            Expense("Transport", 3.00, "Bus ticket", 1699958400000),
            Expense("Loisir", 45.00, "New shoes", 1699962000000),
            Expense("Maison", 15.00, "Movie night", 1699965600000),
            Expense("Depense quotidienne", 14.50, "Lunch at a restaurant mardi", 1699954800000),
            Expense("Transport", 3.00, "Bus ticket", 1699958400000),
            Expense("Loisir", 45.00, "New shoes", 1699962000000),
            Expense("Maison", 15.00, "Movie night", 1699965600000),
            Expense("Depense quotidienne", 15.50, "Lunch at a restaurant mercredi", 1699954800000),
            Expense("Transport", 3.00, "Bus ticket", 1699958400000),
            Expense("Loisir", 45.00, "New shoes", 1699962000000),
            Expense("Maison", 15.00, "Movie night", 1699965600000)
        )


        val consolidatedExpenses = consolidateExpenses(expenses)


        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_month)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(consolidatedExpenses, isAnnualView = false, isMonthFragment= true)

    }



    private fun consolidateExpenses(expenses: List<Expense>): List<Expense> {
        val groupedExpenses = expenses.groupBy { it.category }

        return groupedExpenses.map { (category, categoryExpenses) ->
            val totalPrice = categoryExpenses.sumOf { it.price }
            val concatenatedDescriptions = categoryExpenses.joinToString(separator = "\n") { it.description }
            val descriptionWithPrices = categoryExpenses.map { "${it.price}€" }
            val descriptionWithPricesStr = descriptionWithPrices.joinToString(separator = "\n")
            Expense(
                category = category,
                price = totalPrice,
                description = concatenatedDescriptions,
                date = 0
            ).apply {
                this.detailPrices = descriptionWithPricesStr
            }
        }
    }
}
