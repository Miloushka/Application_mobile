package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton


class MainActivity :BaseActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addCategorie = findViewById<ImageButton>(R.id.button_open_spinner)

        // Redirection vers SpinnerActivity lors du clic sur le bouton
        addCategorie.setOnClickListener {
            val intent = Intent(this, AddCategorieActivity::class.java)
            startActivity(intent)
        }

        setupBottomNavigation(R.id.bottom_navigation_home, R.id.bottom_home)
    }
}