package com.example.suggestion

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suggestion.data.AppDatabase
import com.example.suggestion.data.Depense
import kotlinx.coroutines.launch
import androidx.room.Room
import com.example.suggestion.data.DepenseDao

class HomeFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var depenseDao: DepenseDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "app-database"
        ).build()
        depenseDao = db.depenseDao()

        // Charger les données depuis la base de données de manière asynchrone
        lifecycleScope.launch {
            val expenses = loadExpensesFromDatabase()
            // Configurer RecyclerView une fois les données chargées
            val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_expenses)
            recyclerView.layoutManager = LinearLayoutManager(context)

            // Initialiser l'adaptateur avec les dépenses
            val adapter = ExpenseAdapter(expenses, isAnnualView = false, isMonthFragment = false)

            // Gérer le clic sur une dépense
            adapter.setOnExpenseClickListener { expense ->
                // Lorsque l'utilisateur clique sur une dépense, ouvrir le fragment d'édition
                val fragment = EditExpenseFragment()
                val bundle = Bundle()
                bundle.putParcelable("expense", expense)
                fragment.arguments = bundle

                // Remplacer le fragment actuel par celui d'édition
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment) // Remplacer avec le conteneur approprié
                    .addToBackStack(null) // Ajouter à la pile arrière pour pouvoir revenir en arrière
                    .commit()
            }

            // Définir l'adaptateur
            recyclerView.adapter = adapter
        }
    }

    private suspend fun loadExpensesFromDatabase(): List<Expense> {
        // Charger les dépenses depuis la base de données
        val depenses = depenseDao.getAllDepenses()
        // Mapper les résultats pour les utiliser dans la liste
        return depenses.map {
            Expense(
                id = it.id,
                category = it.category,
                price = it.price,
                description = it.description,
                date = it.date
            )
        }
    }
}
