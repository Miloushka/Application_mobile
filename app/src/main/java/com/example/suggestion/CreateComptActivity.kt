// Cette activité gère la connexion des utilisateurs en vérifiant leurs identifiants.
// Elle redirige vers l'activité principale (`MainActivity`) après une connexion réussie
// ou affiche des messages d'erreur en cas d'échec. Elle propose également des options pour
// créer un compte ou accéder directement à l'application.

package com.example.suggestion;
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CreateComptActivity : AppCompatActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState,)
        setContentView(R.layout.activity_create_compt)

        val email2 = findViewById<EditText>(R.id.Email2)
        val password2 = findViewById<EditText>(R.id.Motdepasse2)
        val password3 = findViewById<EditText>(R.id.Motdepasse3)
        val buttonSeconnecter2 = findViewById<Button>(R.id.seConnecter2)
        val error2 = findViewById<TextView>(R.id.error2)

        //         <!--Trouver le bouton et définir un listener pour le clic -->

        buttonSeconnecter2.setOnClickListener {

            error2.visibility = View.GONE

            val txtEmail2 = email2.text.toString()
            val txtPassword2 = password2.text.toString()
            val txtPassword3 = password3.text.toString()
            if (txtEmail2.trim().isEmpty() || txtPassword2.trim().isEmpty() || txtPassword3.trim().isEmpty()) {
                error2.text = "vous devez remplir tout les champs !"
            
                error2.visibility = View.VISIBLE
            } else if (txtPassword2 != txtPassword3) {
                // Vérifier si les mots de passe correspondent
                error2.text = "Les mots de passe ne correspondent pas !"
                error2.visibility = View.VISIBLE
            } else {
                // Si tout est correct, rediriger vers MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}




