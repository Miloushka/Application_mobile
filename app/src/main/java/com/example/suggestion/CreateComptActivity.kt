// Cette activité gère la connexion des utilisateurs en vérifiant leurs identifiants.
// Elle redirige vers l'activité principale (`MainActivity`) après une connexion réussie
// ou affiche des messages d'erreur en cas d'échec. Elle propose également des options pour
// créer un compte ou accéder directement à l'application.

package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.UserViewModel
import com.example.suggestion.data.UserViewModelFactory

class CreateComptActivity : AppCompatActivity() {

    private lateinit var email2: EditText
    private lateinit var password2: EditText
    private lateinit var password3: EditText
    private lateinit var buttonSeconnecter2: Button
    private lateinit var error2: TextView

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_compt)

        // Obtenir une instance de la base de données
        val database = DataBase.getDatabase(this)
        val userDao = database.userDao()

        // Initialiser le ViewModel avec le UserViewModelFactory
        val factory = UserViewModelFactory(userDao)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        //         <!--Trouver le bouton et définir un listener pour le clic -->

        // Initialiser les vues
        email2 = findViewById(R.id.Email2)
        password2 = findViewById(R.id.Motdepasse2)
        password3 = findViewById(R.id.Motdepasse3)
        buttonSeconnecter2 = findViewById(R.id.seConnecter2)
        error2 = findViewById(R.id.error2)

        // Définir l'écouteur sur le bouton
        buttonSeconnecter2.setOnClickListener {
            validateAndCreateUser()
        }
    }

    private fun validateAndCreateUser() {
        error2.visibility = View.GONE

        val txtEmail2 = email2.text.toString()
        val txtPassword2 = password2.text.toString()
        val txtPassword3 = password3.text.toString()

        when {
            txtEmail2.trim().isEmpty() || txtPassword2.trim().isEmpty() || txtPassword3.trim().isEmpty() -> {
                showError("Vous devez remplir tous les champs !")
            }
            txtPassword2 != txtPassword3 -> {
                showError("Les mots de passe ne correspondent pas !")
            }
            else -> {
                userViewModel.createUser(
                    password = txtPassword2,
                    email = txtEmail2,
                    onSuccess = { user ->
                        Toast.makeText(this, "Inscription réussie ! Bienvenue, ${user.email}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onError = { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun showError(message: String) {
        error2.text = message
        error2.visibility = View.VISIBLE
    }
}




