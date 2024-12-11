package com.example.suggestion

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.suggestion.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView

var userConnected: Long = 0

class MainActivity : AppCompatActivity(),
    EditExpenseFragment.OnExpenseUpdatedListener,
    HomeFragment.OnExpenseDeletedListener {

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

    override fun onExpenseDeleted() {
        val homeFragment = supportFragmentManager.findFragmentByTag("HomeFragment") as? HomeFragment
        homeFragment?.refreshExpenses()
    }

    // Implémentation de la méthode de l'interface OnExpenseUpdatedListener
    override fun onExpenseUpdated() {
        // Cette méthode est appelée lorsque les informations d'une dépense sont mises à jour
        // Vous pouvez mettre à jour la liste des dépenses dans HomeFragment
        val homeFragment = supportFragmentManager.findFragmentByTag("HomeFragment") as? HomeFragment
        homeFragment?.refreshExpenses() // Appeler la méthode publique refreshExpenses pour recharger les dépenses
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
