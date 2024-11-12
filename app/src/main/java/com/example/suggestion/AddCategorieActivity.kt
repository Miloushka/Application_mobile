package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.suggestion.SelectDateHelper.showDatePicker


class AddCategorieActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var customSpinner: Spinner
    private lateinit var expenseDetail: EditText
    private lateinit var priceCost: EditText
    private lateinit var monthDepenseEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_categorie)

        customSpinner = findViewById(R.id.customSpinner)
        customSpinner.onItemSelectedListener = this
        expenseDetail = findViewById(R.id.expenseDetail)
        priceCost = findViewById(R.id.priceCost)
        monthDepenseEditText = findViewById(R.id.monthDepense)
        submitButton = findViewById(R.id.buttonSubmit)


        val customList = ArrayList<CategorieListItems>()
        customList.add(CategorieListItems("Courses", R.drawable.ic_shopping))
        customList.add(CategorieListItems("Maison", R.drawable.ic_home))
        customList.add(CategorieListItems("Loisir", R.drawable.ic_loisir))


        val adapter = CustomAdapter(this, customList)
        customSpinner.adapter = adapter

        // Ouvrir le sélecteur de date lors du clic sur l'EditText
        monthDepenseEditText.setOnClickListener {
            showDatePicker(this, DatePickerMode.MONTH) { selectedDate ->
                // Afficher la date sélectionnée dans l'EditText
                monthDepenseEditText.setText(selectedDate)
                Toast.makeText(this, "Sélectionné le mois de votre dépense : $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }

        submitButton.setOnClickListener {
            val expenseDetailText = expenseDetail.text.toString()
            val priceCostText = priceCost.text.toString()
            val monthDepenseText = monthDepenseEditText.text.toString()

            // Vérifier que tous les champs sont remplis
            if (expenseDetailText.isNotEmpty() && priceCostText.isNotEmpty() && monthDepenseText.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Veuillez saisir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        // Aucune action spécifique lorsque rien n'est sélectionné
    }


    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = adapterView?.getItemAtPosition(position) as CategorieListItems
        Toast.makeText(this, item.spinnerText, Toast.LENGTH_SHORT).show()
    }
}