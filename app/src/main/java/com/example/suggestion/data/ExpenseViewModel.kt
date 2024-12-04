package com.example.suggestion.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    fun getExpenses(userId: Int, onResult: (List<Expense>) -> Unit) {
        viewModelScope.launch {
            val expenses = expenseDao.getExpensesForUser(userId)
            onResult(expenses)
        }
    }

    fun addExpense(userId: Int, amount: Double, description: String, date: String, categorie: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            expenseDao.insertExpense(Expense(0, userId, amount, description, date, categorie))
            onComplete()
        }
    }
}
