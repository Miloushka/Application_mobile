package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import androidx.room.Room
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.ExpenseDao

class HomeFragment : Fragment() {

    private lateinit var db: DataBase
    private lateinit var expenseDao: ExpenseDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser la base de données
        db = Room.databaseBuilder(
            requireContext(),
            DataBase::class.java, "database"
        ).build()
        expenseDao = db.expenseDao()

        // Initialisation du bouton pour ajouter une dépense
        val addButton: ImageButton = view.findViewById(R.id.button_open_add_categorie)
        addButton.setOnClickListener {
            openAddCategorieDialog()
        }

        // Charger les données depuis la base de données
        loadExpenses()
    }

    // Fonction pour charger les dépenses depuis la base de données
    private fun loadExpenses() {
        lifecycleScope.launch {
            val expenses = loadExpensesFromDatabase()

            // Configurer RecyclerView
            val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view_expenses)
            recyclerView.layoutManager = LinearLayoutManager(context)

            //Initialiser l'adaptateur avec les dépenses
            val adapter = ExpenseAdapter(expenses, isAnnualView = false, isMonthFragment = false)

            // Définir le gestionnaire de clic sur les éléments de la liste
            adapter.setOnExpenseClickListener { expense ->
                // Lorsque l'utilisateur clique sur une dépense, ouvrez le fragment d'édition
                openEditExpenseFragment(expense)
            }
            recyclerView.adapter = adapter

            // Vérifier si la liste des dépenses est vide et afficher le message
            val noExpensesMessage: TextView = requireView().findViewById(R.id.no_expenses_message)
            if (expenses.isEmpty()) {
                noExpensesMessage.visibility = View.VISIBLE  // Afficher le message si aucune dépense
                recyclerView.visibility = View.GONE         // Masquer le RecyclerView
            } else {
                noExpensesMessage.visibility = View.GONE   // Masquer le message
                recyclerView.visibility = View.VISIBLE     // Afficher le RecyclerView
            }
        }
    }

    // Fonction pour charger les dépenses depuis la base de données
    private suspend fun loadExpensesFromDatabase(): List<ExpenseApp> {
        val depenses = expenseDao.getAllExpenses()
        return depenses.map {
            ExpenseApp(
                userId = it.userId,
                expenseId = it.expenseId,
                category = it.category,
                price = it.amount,
                description = it.description,
                date = it.date
            )
        }
    }

    // Fonction pour ouvrir EditExpenseFragment
    private fun openEditExpenseFragment(expense: ExpenseApp) {
        val editExpenseFragment = EditExpenseFragment()

        // Passer l'objet ExpenseApp au fragment via un Bundle
        val bundle = Bundle()
        bundle.putParcelable("expense", expense)
        editExpenseFragment.arguments = bundle

        // Remplacer le fragment actuel par EditExpenseFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editExpenseFragment)
            .addToBackStack(null) // Ajouter à la pile de fragments pour permettre de revenir en arrière
            .commit()
    }

    // Fonction pour ouvrir AddCategorieDialogFragment
    private fun openAddCategorieDialog() {
        val addCategorieDialogFragment = AddCategorieDialogFragment()

        // Définir le callback pour mettre à jour la liste des dépenses après ajout
        addCategorieDialogFragment.onDepenseAddedListener = {
            // Recharger les dépenses après l'ajout
            loadExpenses()
        }

        addCategorieDialogFragment.show(childFragmentManager, "AddCategorieDialogFragment")
    }

    fun refreshExpenses() {
        loadExpenses()  // Appeler la méthode privée pour recharger les dépenses
    }

    interface OnExpenseDeletedListener {
        fun onExpenseDeleted()
    }
}
