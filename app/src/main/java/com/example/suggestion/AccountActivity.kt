package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.suggestion.SelectDateHelper.showDatePicker

class AccountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Configuration de la navigation
        setupBottomNavigation(R.id.bottom_navigation_account, R.id.bottom_account)

        // Redirection vers ResetPasswordActivity lors du clic sur le texte
        val resetPassword = findViewById<TextView>(R.id.resetpassword)
        resetPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }


        // Référence à l'EditText pour la date de naissance
        val dateOfBirthEditText = findViewById<EditText>(R.id.dateOfBirth)

        // Ouvrir le sélecteur de date lors du clic sur l'EditText
        dateOfBirthEditText.setOnClickListener {
            showDatePicker(this, DatePickerMode.FULL_DATE) { selectedDate ->
                // Afficher la date sélectionnée dans l'EditText
                dateOfBirthEditText.setText(selectedDate)
                Toast.makeText(this, "Anniversaire sélectionné : $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

