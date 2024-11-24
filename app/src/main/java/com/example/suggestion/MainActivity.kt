// Cette activité sert de point d'entrée pour l'application et contient un BottomNavigationView
// pour permettre la navigation entre différents fragments (Home, Month, Annual, Account).
// Elle charge un fragment par défaut (HomeFragment) et permet de naviguer vers d'autres fragments
// à l'aide du menu de navigation. En outre, elle détecte les clics en dehors des EditText pour
// masquer le clavier virtuel lorsque l'utilisateur interagit avec l'interface.

package com.example.suggestion

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI(findViewById(android.R.id.content))

        // Récupérer le signal de l'Intent pour savoir s'il faut charger le AccountFragment
        val loadAccountFragment = intent.getBooleanExtra("LOAD_ACCOUNT_FRAGMENT", false)
        // Configuration de la navigation par fragments
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_home)

        // Si le signal est true, chargez le AccountFragment
        if (loadAccountFragment) {
            loadFragment(AccountFragment())  // Charge le fragment Account
            bottomNavigationView.selectedItemId = R.id.bottom_account
        } else {
            if (savedInstanceState == null) {
                loadFragment(HomeFragment())  // Charge le fragment Home
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> loadFragment(HomeFragment())
                R.id.bottom_month -> loadFragment(MonthFragment())
                R.id.bottom_account -> loadFragment(AccountFragment())
                R.id.bottom_annual -> loadFragment(AnnualFragment())
            }
            true
        }
    }

    // Méthode pour configurer la détection de clic en dehors des EditTexts pour fermer le clavier
    private fun setupUI(view: View) {
        // Appliquer un OnTouchListener pour cacher le clavier si on clique en dehors des EditText
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        // Itérer sur tous les enfants si la vue est un groupe
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    // Méthode pour masquer le clavier
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
        private fun loadFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

    }
}