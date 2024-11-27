// Ce fragment permet d'ajouter une nouvelle catégorie de dépense via une interface modale (dialog),
// avec un sélecteur de date et un spinner pour choisir la catégorie.
// Lors de la soumission, si tous les champs sont remplis, la dépense est ajoutée.

package com.example.suggestion

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.suggestion.SelectDateHelper.showDatePicker

class AddCategorieDialogFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var customSpinner: Spinner
    private lateinit var expenseDetail: EditText
    private lateinit var priceCost: EditText
    private lateinit var monthDepenseEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_add_categorie, container, false)

        customSpinner = view.findViewById(R.id.customSpinner)
        customSpinner.onItemSelectedListener = this
        expenseDetail = view.findViewById(R.id.expenseDetail)
        priceCost = view.findViewById(R.id.priceCost)
        monthDepenseEditText = view.findViewById(R.id.monthDepense)
        submitButton = view.findViewById(R.id.buttonSubmit)

        val customList = arrayListOf(
            CategorieListItems("Dépenses quotidiennes", R.drawable.ic_shopping),
            CategorieListItems("Maison", R.drawable.ic_home),
            CategorieListItems("Loisir", R.drawable.ic_loisir)
        )

        val adapter = CustomAdapter(requireContext(), customList)
        customSpinner.adapter = adapter

        // Ouvrir le sélecteur de date lors du clic sur l'EditText
        monthDepenseEditText.setOnClickListener {
            showDatePicker(requireContext()) { selectedDate ->
                monthDepenseEditText.setText(selectedDate)
                Toast.makeText(context, "Sélectionné la date de votre dépense : $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }

        submitButton.setOnClickListener {
            val expenseDetailText = expenseDetail.text.toString()
            val priceCostText = priceCost.text.toString()
            val monthDepenseText = monthDepenseEditText.text.toString()

            if (expenseDetailText.isNotEmpty() && priceCostText.isNotEmpty() && monthDepenseText.isNotEmpty()) {
                dismiss() // Ferme la modale
                Toast.makeText(context, "Dépense ajoutée avec succès", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Veuillez saisir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = adapterView?.getItemAtPosition(position) as CategorieListItems
        Toast.makeText(context, item.spinnerText, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setTitle("Ajouter une catégorie")
        }
    }
}
