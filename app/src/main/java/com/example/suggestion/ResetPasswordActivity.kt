// Activity qui permet de réinisialiser son mot de passe

package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.UserViewModel
import com.example.suggestion.data.UserViewModelFactory

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var passwordInput: EditText
    private lateinit var newPasswordInput: EditText

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Obtenir une instance de la base de données
        val database = DataBase.getDatabase(this)
        val userDao = database.userDao()

        // Initialiser le ViewModel avec le UserViewModelFactory
        val factory = UserViewModelFactory(userDao)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        // Initialiser les vues
        passwordInput = findViewById(R.id.actualPasswd)
        newPasswordInput = findViewById(R.id.newPasswd)


        val resetPassword = findViewById<TextView>(R.id.buttonSubmit)

        // Redirection vers AccountActivity lors du clic sur le bouton
        resetPassword.setOnClickListener {

            userViewModel.changePassword(
                userIdConnected,
                passwordInput.text.toString(),
                newPasswordInput.text.toString(),
                onSuccess = { user ->
                    Toast.makeText(this, "Inscription réussie ! Bienvenue, ${user.email}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            )

            // Rediriger vers MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("LOAD_ACCOUNT_FRAGMENT", true)
            startActivity(intent)
            finish()
        }
    }
}