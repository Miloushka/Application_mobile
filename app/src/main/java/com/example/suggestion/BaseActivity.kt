package com.example.suggestion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

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
}
