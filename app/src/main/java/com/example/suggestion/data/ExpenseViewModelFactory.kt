package com.example.suggestion.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ExpenseViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Class ViewModel inconnue")
    }
}