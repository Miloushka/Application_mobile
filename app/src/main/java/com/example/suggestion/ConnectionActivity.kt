// Cette activité gère la connexion des utilisateurs en validant leurs identifiants.
// Elle offre des options pour accéder à l'application principale (`MainActivity`) après une connexion réussie,
// ou pour créer un nouveau compte en redirigeant vers l'activité `CreateComptActivity`.
// Elle affiche des messages d'erreur en cas de champs vides ou d'identifiants incorrects.

package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


import com.example.suggestion.data.DataBase
import com.example.suggestion.data.UserViewModel
import com.example.suggestion.data.UserViewModelFactory

class ConnectionActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var errorTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)


        // Obtenir une instance de la base de données
        val database = DataBase.getDatabase(this)
        val userDao = database.userDao()

        // Initialiser le ViewModel avec le UserViewModelFactory
        val factory = UserViewModelFactory(userDao)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        // Initialiser les vues
        emailInput = findViewById(R.id.email)
        passwordInput = findViewById(R.id.motdepasse)
        errorTextView = findViewById(R.id.error)
        loginButton = findViewById(R.id.seConnecter)
        createAccountButton = findViewById(R.id.create_compte)

        // Logique du bouton de connexion
        loginButton.setOnClickListener {
            handleLogin()
        }

        // Logique du bouton de création de compte
        createAccountButton.setOnClickListener {
            val intent = Intent(this, CreateComptActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun handleLogin() {
        errorTextView.visibility = View.GONE

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            showError("Vous devez remplir tous les champs !")
        } else {
            // Appeler la méthode de connexion dans le ViewModel
            userViewModel.login(
                email = email,
                password = password,
                onSuccess = { user ->
                    Toast.makeText(
                        this,
                        "Connexion réussie ! Bienvenue, ${user.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                onError = { message ->
                    showError(message)
                }
            )
        }
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }
}

