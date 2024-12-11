// Ce fragment permet de d'afficher les informations personnelles de l'utilisateur, redirection vers l'activity ResetPassword

package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.suggestion.SelectDateHelper.showDatePicker

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupérez l'EditText par son ID
        val editText: EditText = findViewById(R.id.editText)

        // Définissez la valeur initiale de l'EditText
        editText.setText("Prérempli")

        // Redirection vers ResetPasswordActivity lors du clic sur le texte
        val resetPassword = view.findViewById<TextView>(R.id.resetpassword)
        resetPassword.setOnClickListener {
            val intent = Intent(requireActivity(), ResetPasswordActivity::class.java)
            startActivity(intent)
        }


        // Référence à l'EditText pour la date de naissance
        val dateOfBirthEditText = view.findViewById<EditText>(R.id.dateOfBirth)

        // Ouvrir le sélecteur de date lors du clic sur l'EditText
        dateOfBirthEditText.setOnClickListener {
            showDatePicker(requireContext()) { selectedDate ->
                // Afficher la date sélectionnée dans l'EditText
                dateOfBirthEditText.setText(selectedDate)
                Toast.makeText(requireContext(), "Anniversaire sélectionné : $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }

    }
}