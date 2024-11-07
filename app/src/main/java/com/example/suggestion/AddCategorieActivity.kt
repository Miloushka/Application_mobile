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


class AddCategorieActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var customSpinner: Spinner
    private lateinit var inputField: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_categorie)

        customSpinner = findViewById(R.id.customSpinner)
        customSpinner.onItemSelectedListener = this
        inputField = findViewById(R.id.inputField)
        submitButton = findViewById(R.id.buttonSubmit)


        val customList = ArrayList<CategorieListItems>()
        customList.add(CategorieListItems("Courses", R.drawable.ic_shopping))
        customList.add(CategorieListItems("Maison", R.drawable.ic_home))
        customList.add(CategorieListItems("Loisir", R.drawable.ic_loisir))


        val adapter = CustomAdapter(this, customList)
        customSpinner.adapter = adapter

        submitButton.setOnClickListener {
            val enteredText = inputField.text.toString()

            if (enteredText.isNotEmpty()) {
                // Affichage du texte saisi par l'utilisateur
                Toast.makeText(this, "Texte saisi : $enteredText", Toast.LENGTH_SHORT).show()

                // Rediriger vers la page principale
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // Fermer l'activité courante (AddCategorieActivity)
                finish()
            } else {
                // Message si le champ de saisie est vide
                Toast.makeText(this, "Veuillez saisir du texte", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = adapterView?.getItemAtPosition(position) as CategorieListItems
        Toast.makeText(this, item.spinnerText, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {

    }
}