package com.example.suggestion.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggestion.expensesUserConnected
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    lateinit var expense: Expense

    fun getExpenses(userId: Long) {
        viewModelScope.launch {
            expensesUserConnected = expenseDao.getExpensesForUser(userId)
        }
    }

    //userId: Int, amount: Double, description: String, date: String, categorie: String
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insertExpense(expense)
        }
    }
}
