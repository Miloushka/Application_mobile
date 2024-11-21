package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

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

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_expenses)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(expenses, isAnnualView = true)


    }
}
