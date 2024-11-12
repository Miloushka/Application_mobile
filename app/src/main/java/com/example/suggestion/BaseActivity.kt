package com.example.suggestion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure la détection de clic pour fermer le clavier
        setupUI(findViewById(android.R.id.content))
    }

    // Méthode pour configurer la barre de navigation inférieure
    protected fun setupBottomNavigation(bottomNavigationId: Int, selectedItemId: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(bottomNavigationId)
        // Vérifie si la BottomNavigationView existe
        bottomNavigationView?.let {
            // Définit l'élément actif dans la barre de navigation
            it.selectedItemId = selectedItemId
            // Associe chaque élément de navigation à une activité spécifique
            val activityMap = mapOf(
                R.id.bottom_home to MainActivity::class.java,
                R.id.bottom_month to MonthActivity::class.java,
                R.id.bottom_account to AccountActivity::class.java,
                R.id.bottom_annual to AnnualActivity::class.java
            )

            // Gère les clics sur les éléments de navigation
            it.setOnItemSelectedListener { item ->
                val selectedActivity = activityMap[item.itemId]

                // Si une activité est associée à l'item cliqué et que ce n'est pas l'item actif
                if (selectedActivity != null && selectedItemId != item.itemId) {
                    // Lance l'activité associée
                    val intent = Intent(this, selectedActivity)
                    startActivity(intent)
                    finish() // Ferme l'activité actuelle pour éviter les doublons

                    // Applique les animations de transition
                    //overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
                }
                true
            }
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
}
