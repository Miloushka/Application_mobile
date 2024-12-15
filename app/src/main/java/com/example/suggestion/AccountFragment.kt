// Ce fragment permet de d'afficher les informations personnelles de l'utilisateur, redirection vers l'activity ResetPassword

package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.suggestion.SelectDateHelper.showDatePicker
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.UserViewModelFactory

class AccountFragment : Fragment() {

    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var inputName: EditText
    lateinit var inputSurname: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Récupérez l'EditText par son ID
        inputEmail = view.findViewById(R.id.input_email)
        inputPassword = view.findViewById(R.id.input_password)
        inputName = view.findViewById(R.id.input_last_name)
        inputSurname = view.findViewById(R.id.input_first_name)

        // Définissionla valeur initiale de l'EditText
        inputEmail.setText(userConnected.email)
        inputPassword.setText(userConnected.password)
        inputName.setText(userConnected.lastName)
        inputSurname.setText(userConnected.firstName)

        // Redirection vers ResetPasswordActivity lors du clic sur le texte
        val resetPassword = view.findViewById<TextView>(R.id.resetpassword)
        resetPassword.setOnClickListener {
            val intent = Intent(requireActivity(), ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        // Référence à l'EditText pour la date de naissance
        val dateOfBirthEditText = view.findViewById<EditText>(R.id.dateOfBirth)
        dateOfBirthEditText.setText(userConnected.dateOfBirth)
        // Ouvrir le sélecteur de date lors du clic sur l'EditText
        dateOfBirthEditText.setOnClickListener {
            showDatePicker(requireContext()) { selectedDate ->
                // Afficher la date sélectionnée dans l'EditText
                dateOfBirthEditText.setText(selectedDate)
                Toast.makeText(requireContext(), "Anniversaire sélectionné : $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }

        val validation = view.findViewById<Button>(R.id.validation)
        validation.setOnClickListener{
            var email = inputEmail.text.toString()
            var name = inputName.text.toString()
            var firstname= inputSurname.text.toString()
            var dateOfBirth = dateOfBirthEditText.text.toString()
            userConnected = userConnected.copy(email=email, lastName = name, firstName = firstname, dateOfBirth = dateOfBirth)
        }


    }
}