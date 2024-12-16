package com.example.suggestion


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.Expense
import com.example.suggestion.data.ExpenseViewModel
import com.example.suggestion.data.ExpenseViewModelFactory
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditExpenseFragment : Fragment() {

    private var onExpenseUpdatedListener: OnExpenseUpdatedListener? = null
    private lateinit var customSpinner: Spinner
    private lateinit var expenseDetail: EditText
    private lateinit var priceCost: EditText
    private lateinit var monthDepenseEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var deleteButton: Button
    private lateinit var expenseViewModel: ExpenseViewModel

    private var selectedCategory: String = ""

    private val customList = arrayListOf(
        CategorieListItems("Dépense quotidienne", R.drawable.ic_shopping),
        CategorieListItems("Maison", R.drawable.ic_home),
        CategorieListItems("Loisir", R.drawable.ic_loisir),
        CategorieListItems("Transport", R.drawable.ic_transport),
        CategorieListItems("Revenu", R.drawable.ic_income)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser la base de données
        val database = DataBase.getDatabase(requireContext())
        val expanseDao = database.expenseDao()
        // Initialiser le ViewModel
        val factory = ExpenseViewModelFactory(expanseDao)
        expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]


        // Initialisation des vues
        customSpinner = view.findViewById(R.id.customSpinner)
        expenseDetail = view.findViewById(R.id.description_edit_text)
        priceCost = view.findViewById(R.id.price_edit_text)
        monthDepenseEditText = view.findViewById(R.id.date_edit_text)
        submitButton = view.findViewById(R.id.save_button)
        deleteButton = view.findViewById(R.id.delete_button)



        // Initialiser le Spinner avec l'adaptateur personnalisé
        val adapter = CustomAdapter(requireContext(), customList)
        customSpinner.adapter = adapter

        customSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) as CategorieListItems
                selectedCategory = selectedItem.spinnerText
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCategory = "Autre"
            }
        }


        // Remplir les champs avec les valeurs de la dépense actuelle
        expenseCurrent?.let { exp ->
            // Remplir les champs de texte
            expenseDetail.setText(exp.description)
            priceCost.setText(exp.amount.toString())
            monthDepenseEditText.setText(formatDate(exp.date))

            // Sélectionner la catégorie correspondante dans le Spinner
            val categoryIndex = customList.indexOfFirst { it.spinnerText == exp.category }
            if (categoryIndex != -1) {
                customSpinner.setSelection(categoryIndex)
                selectedCategory = exp.category
            }
        }


        // Sauvegarder les modifications
        val saveButton: Button = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val updatedCategory = customSpinner.selectedItem.toString()
            val updatedDescription = expenseDetail.text.toString()
            val updatedPrice = priceCost.text.toString().toDouble()
            val updatedDate = monthDepenseEditText.text.toString()
            val updatedExpense = expenseCurrent.copy(amount = updatedPrice, date = updatedDate, description = updatedDescription, category = updatedCategory)
            expenseViewModel.updateExpense(updatedExpense)
            expenseCurrent = updatedExpense

            // Fermer le fragment
            parentFragmentManager.popBackStack()
        }

        val deleteButton :Button = view.findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            // Afficher la boîte de dialogue de confirmation
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Voulez-vous vraiment supprimer cette dépense ?")
            .setPositiveButton("Oui") { dialog, which ->
                // Si l'utilisateur confirme la suppression, on la supprime de la base de données
                deleteExpense()


            }
            .setNegativeButton("Non") { dialog, which ->
                // Si l'utilisateur annule, on ferme simplement le dialogue
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }



    // Formater la date en dd/MM/yyyy
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: ParseException) {
            ""
        }
    }

    // Pour que HomeFragment puisse définir le listener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnExpenseUpdatedListener) {
            onExpenseUpdatedListener = context
        } else {
            throw ClassCastException("$context must implement OnExpenseUpdatedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onExpenseUpdatedListener = null
    }


    private fun deleteExpense() {
        lifecycleScope.launch {
            expenseCurrent?.let { exp ->
                // Supprimer la dépense de la base de données
                expenseViewModel.deleteExpense(expenseCurrent)

                // Notifier le HomeFragment que la dépense a été supprimée
                (activity as? MainActivity)?.onExpenseDeleted()

                // Fermer le fragment actuel
                parentFragmentManager.popBackStack()
            }
        }
    }

    interface OnExpenseUpdatedListener {
        fun onExpenseUpdated()
    }

}
