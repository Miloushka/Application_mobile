package com.example.suggestion

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.Expense
import com.example.suggestion.data.ExpenseViewModel
import com.example.suggestion.data.ExpenseViewModelFactory
import com.example.suggestion.data.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddCategorieDialogFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var customSpinner: Spinner
    private lateinit var expenseDetail: EditText
    private lateinit var priceCost: EditText
    private lateinit var monthDepenseEditText: EditText
    private lateinit var submitButton: Button

    // Room Database instances
    private lateinit var userViewModel: UserViewModel
    private lateinit var expenseViewModel: ExpenseViewModel

    private var selectedCategory: String = ""
    //private var userId: Int = 1

    // Callback pour informer HomeFragment que la dépense a été ajoutée
    var onDepenseAddedListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_add_categorie, container, false)


        // Initialiser la base de données
        val database = DataBase.getDatabase(requireContext())
        val expanseDao = database.expenseDao()
        // Initialiser le ViewModel
        val factory = ExpenseViewModelFactory(expanseDao)
        expenseViewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        customSpinner = view.findViewById(R.id.customSpinner)
        customSpinner.onItemSelectedListener = this
        expenseDetail = view.findViewById(R.id.expenseDetail)
        priceCost = view.findViewById(R.id.priceCost)
        monthDepenseEditText = view.findViewById(R.id.monthDepense)
        submitButton = view.findViewById(R.id.buttonSubmit)

        val customList = arrayListOf(
            CategorieListItems("Dépense quotidienne", R.drawable.ic_shopping),
            CategorieListItems("Maison", R.drawable.ic_home),
            CategorieListItems("Loisir", R.drawable.ic_loisir),
            CategorieListItems("Transport", R.drawable.ic_transport),
            CategorieListItems("Revenu", R.drawable.ic_income)
        )
        val adapter = CustomAdapter(requireContext(), customList)
        customSpinner.adapter = adapter

        monthDepenseEditText.setOnClickListener {
            showDatePicker { selectedDate ->
                monthDepenseEditText.setText(selectedDate)
                Toast.makeText(context, "Date sélectionnée : $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }

        submitButton.setOnClickListener {
            val expenseDetailText = expenseDetail.text.toString().trim()
            val priceCostText = priceCost.text.toString().trim()
            val monthDepenseText = monthDepenseEditText.text.toString().trim()

            if (expenseDetailText.isNotEmpty() && priceCostText.isNotEmpty() && monthDepenseText.isNotEmpty()) {
                if (isValidDate(monthDepenseText)) {
                    val category = if (selectedCategory.isNotEmpty()) selectedCategory else "Autre"
                    val newExpenseApp= Expense(
                        userId = userConnected.userId,
                        expenseId = generateNewId(),
                        category = category,
                        amount = priceCostText.toDouble(),
                        description = expenseDetailText,
                        date = monthDepenseText
                    )

                    lifecycleScope.launch {
                        val newExpense = mapToExpense(newExpenseApp)
                        insertDepenseToDatabase(newExpense)
                        Toast.makeText(context, "Dépense ajoutée avec succès", Toast.LENGTH_SHORT).show()

                        // Appeler le callback pour informer le fragment parent
                        onDepenseAddedListener?.invoke()

                        dismissAllowingStateLoss()
                    }
                } else {
                    Toast.makeText(context, "Date invalide. Utilisez le format AAAA-MM-JJ", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun generateNewId(): Int {
        return (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.apply { set(year, month, dayOfMonth) }.time)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date) != null
        } catch (e: Exception) {
            false
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position) as CategorieListItems
        selectedCategory = selectedItem.spinnerText
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        selectedCategory = "Autre"
    }

    // Fonction de mappage de ExpenseApp vers Expense
    private fun mapToExpense(expenseApp: Expense): Expense {
        return Expense(
            expenseId = expenseApp.expenseId,
            userId = userConnected.userId,
            amount = expenseApp.amount,
            description = expenseApp.description,
            date = expenseApp.date,
            category = expenseApp.category
        )
    }

    private fun insertDepenseToDatabase(expense: Expense) {
        expenseViewModel.addExpense(expense)
    }
}
