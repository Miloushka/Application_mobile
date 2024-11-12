package com.example.suggestion


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ConnectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

//         <!--Trouver le bouton et définir un listener pour le clic -->
        val button = findViewById<Button>(R.id.btn_start)
        button.setOnClickListener {
//            <!-- Rediriger vers MainActivity après le clic -->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        }
    }
