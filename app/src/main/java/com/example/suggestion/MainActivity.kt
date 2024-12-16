package com.example.suggestion

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.suggestion.data.DataBase
import com.example.suggestion.data.Expense
import com.example.suggestion.data.User
import com.example.suggestion.data.UserViewModel
import com.example.suggestion.data.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

var userIdConnected: Long = 0
var userConnected = User(0, "", "", "", "", "")
var expensesUserConnected = listOf<Expense>()


class MainActivity : AppCompatActivity(),
    EditExpenseFragment.OnExpenseUpdatedListener,
    HomeFragment.OnExpenseDeletedListener {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI(findViewById(android.R.id.content))

        // Initialisation du ViewModel
        val database = DataBase.getDatabase(this)
        val userDao = database.userDao()
        val factory = UserViewModelFactory(userDao)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        // Récupérer les données de l'utilisateur connecté
        userViewModel.getUserById(userIdConnected)

        // Configuration de la navigation par fragments
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_home)

        // Gestion du signal d'Intent pour charger le fragment approprié
        val loadAccountFragment = intent.getBooleanExtra("LOAD_ACCOUNT_FRAGMENT", false)
        if (loadAccountFragment) {
            loadFragment(AccountFragment())  // Charger le fragment Account
            bottomNavigationView.selectedItemId = R.id.bottom_account
        } else {
            if (savedInstanceState == null) {
                loadFragment(HomeFragment())  // Charger le fragment Home
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            userViewModel.updateUser()
            when (item.itemId) {
                R.id.bottom_home -> loadFragment(HomeFragment())
                R.id.bottom_month -> loadFragment(MonthFragment())
                R.id.bottom_account -> loadFragment(AccountFragment())
                R.id.bottom_annual -> loadFragment(AnnualFragment())
            }
            true
        }
    }

    // Implémentation de la méthode de l'interface OnExpenseDeletedListener
    override fun onExpenseDeleted() {
        // Trouver le fragment Home et appeler la méthode refreshExpenses
        val homeFragment = supportFragmentManager.findFragmentByTag("HomeFragment") as? HomeFragment
        homeFragment?.refreshExpenses()  // Rafraîchir les dépenses
    }

    // Implémentation de la méthode de l'interface OnExpenseUpdatedListener
    override fun onExpenseUpdated() {
        // Trouver le fragment Home et appeler la méthode refreshExpenses
        val homeFragment = supportFragmentManager.findFragmentByTag("HomeFragment") as? HomeFragment
        homeFragment?.refreshExpenses()  // Rafraîchir les dépenses
    }

    private fun loadFragment(fragment: Fragment) {
        // Chargement du fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Méthode pour configurer la détection de clics en dehors des EditText pour fermer le clavier
    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
