package com.example.suggestion.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggestion.expensesUserConnected
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    // Utilisation de LiveData pour observer les dépenses
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    // Ajoutez une propriété pour stocker une dépense sélectionnée si nécessaire
    var expense: Expense? = null

    // Fonction pour récupérer les dépenses d'un utilisateur spécifique
    fun getExpenses(userId: Long) {
        viewModelScope.launch {
            expensesUserConnected = expenseDao.getExpensesForUser(userId)
        }
    }

    // Fonction pour ajouter une dépense
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insertExpense(expense)
        }
    }

    // Fonction pour mettre à jour une dépense
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.updateExpense(expense)
        }
    }

    // Fonction pour supprimer une dépense
    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.deleteExpense(expense)
        }
    }
}
