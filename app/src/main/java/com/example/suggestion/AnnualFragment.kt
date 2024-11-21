package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AnnualFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_annual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val annualExpenses = listOf(
            CategoryTotal("Depense quotidienne", 100.00),
            CategoryTotal("Transport", 50.00),
            CategoryTotal("Loisir", 200.00)
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_annual)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ExpenseAdapter(annualExpenses, isAnnualView = true)
    }
}
