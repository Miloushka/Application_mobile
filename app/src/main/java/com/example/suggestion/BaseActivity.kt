package com.example.suggestion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    // Cette méthode configure la barre de navigation avec un ID spécifique pour chaque page
    protected fun setupBottomNavigation(bottomNavigationId: Int, selectedItemId: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(bottomNavigationId)

        // Vérifier que la BottomNavigationView existe avant de la manipuler
        bottomNavigationView?.let {
            // Définir l'élément sélectionné dans la barre de navigation
            it.selectedItemId = selectedItemId

            // Définir une map associant chaque item de la barre de navigation à une activité spécifique
            val activityMap = mapOf(
                R.id.bottom_home to MainActivity::class.java,
                R.id.bottom_month to MonthActivity::class.java,
                R.id.bottom_annual to AnnualActivity::class.java,
                R.id.bottom_setting to SettingsActivity::class.java
            )

            // Gérer l'événement de sélection d'un item dans la barre de navigation
            it.setOnItemSelectedListener { item ->
                val selectedActivity = activityMap[item.itemId]

                // Si une activité valide est associée à l'item sélectionné, on la lance
                if (selectedActivity != null && selectedItemId != item.itemId) {
                    // Lancer l'activité sans appeler finish() pour éviter la fermeture prématurée
                    val intent = Intent(this, selectedActivity)
                    startActivity(intent)

                    // Appliquer des animations de transition lors du changement d'activité
                   // overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
                }
                true
            }
        } 
    }
}
