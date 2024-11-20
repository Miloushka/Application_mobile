package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val resetPassword = findViewById<TextView>(R.id.buttonSubmit)

        // Redirection vers AccountActivity lors du clic sur le bouton
        resetPassword.setOnClickListener {
            // Rediriger vers MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("LOAD_ACCOUNT_FRAGMENT", true)
            startActivity(intent)
            finish()
        }
    }
}