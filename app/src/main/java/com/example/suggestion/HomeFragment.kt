package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.Expense
import com.example.suggestion.data.ExpenseViewModel
import com.example.suggestion.data.ExpenseViewModelFactory
import com.example.suggestion.data.UserViewModel
import com.example.suggestion.data.UserViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser la base de données
        val database = DataBase.getDatabase(requireContext())
        val expenseDao = database.expenseDao()

        // Initialiser le ViewModel
        val factory = ExpenseViewModelFactory(expenseDao)
        expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        // Initialisation du bouton pour ajouter une dépense
        val addButton: ImageButton = view.findViewById(R.id.button_open_add_categorie)
        addButton.setOnClickListener {
            openAddCategorieDialog()
        }

        // Observer les dépenses via LiveData
        expenseViewModel.allExpenses.observe(viewLifecycleOwner, { expenses ->
            // Mettre à jour le RecyclerView avec la nouvelle liste de dépenses
            updateRecyclerView(expenses)
        })
    }

    // Mettre à jour le RecyclerView avec les nouvelles dépenses
    private fun updateRecyclerView(expenses: List<Expense>) {
        // Configurer RecyclerView
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view_expenses)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialiser l'adaptateur avec les dépenses
        val adapter = ExpenseAdapter(expenses, isMonthFragment = false, isAnnualView = false)

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

    // Fonction pour ouvrir EditExpenseFragment
    private fun openEditExpenseFragment(expense: Expense) {
        val editExpenseFragment = EditExpenseFragment()

        // Passer l'objet Expense au fragment via le expenseViewModel
        expenseViewModel.expense = expense

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
            refreshExpenses()
        }

        addCategorieDialogFragment.show(childFragmentManager, "AddCategorieDialogFragment")
    }

    // Fonction pour rafraîchir les dépenses après ajout
    fun refreshExpenses() {
        // Pas nécessaire car LiveData observer gère déjà l'actualisation
    }

    interface OnExpenseDeletedListener {
        fun onExpenseDeleted()
    }
}
