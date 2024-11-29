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

class ConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

        val buttonSeconnecter = findViewById<Button>(R.id.seConnecter)
        //val connect = findViewById<Button>(R.id.seConnecter)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.motdepasse)
        val error = findViewById<TextView>(R.id.error)

        buttonSeconnecter.setOnClickListener {

            error.visibility = View.GONE

            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()
            if (txtEmail.trim().isEmpty() || txtPassword.trim().isEmpty()) {
                error.text = "vous devez remplir tout les champs !"
                error.visibility = View.VISIBLE
            } else {
                val correctEmail = "exemple@gmail.com"
                val correctPassword = "azerty"
                if (correctEmail == txtEmail && correctPassword == txtPassword) {

                    Toast.makeText(
                        this,
                        "Bravo, étes connectée!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    
                    error.text = "Email ou Mot de passe incorrect"
                    error.visibility = View.VISIBLE
                    buttonSeconnecter.isEnabled = true
                }
            }
        }


        //         <!--Trouver le bouton et définir un listener pour le clic -->
        val button = findViewById<Button>(R.id.btn_start)
        button.setOnClickListener {
//            <!-- Rediriger vers MainActivity après le clic -->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        // Trouver le bouton et définir un listener pour le clic
        val buttonCreateCompte = findViewById<Button>(R.id.create_compte)
        buttonCreateCompte.setOnClickListener {
            // Rediriger vers CreateCompteActivity après le clic
            val intent = Intent(this, CreateComptActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

